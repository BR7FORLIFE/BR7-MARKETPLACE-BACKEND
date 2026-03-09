 # Requerimientos — Marketplace Reactivo (Backend)

 Este documento resume los requerimientos técnicos para el proyecto "Marketplace Reactivo". Está organizado para ser claro y accionable: resumen, alcance, requisitos funcionales y no funcionales, diseño de datos, flujos críticos y pasos para ejecutar y probar localmente.

 ---

 ## 1. Resumen

 Construir un backend reactivo para un marketplace (similar a MercadoLibre) usando Spring WebFlux y R2DBC. El sistema debe soportar:
 - Órdenes con reserva y confirmación de stock
 - Pasarela de pagos simulada y compensaciones (SAGA)
 - Seguridad (OAuth2/JWT, refresh tokens, revocación)
 - Observabilidad (trazas, métricas) y resiliencia (outbox, retries)

 El objetivo es entregar un conjunto de servicios modulares (pueden ser módulos en un mono-repo) que muestren dominio correcto, tests y un flujo end-to-end mínimo (orden → pago → envío).

 ## 2. Alcance mínimo (entregables obligatorios)
 - Servicios reactivos: `auth`, `catalog`, `listing`, `inventory`, `order`, `payment` (pueden ser módulos).
 - Flow happy-path: crear orden, reservar inventario, procesar pago (simulado), confirmar orden. Incluir al menos una compensación (ej. pago fallido → liberar stock).
 - Outbox pattern y un worker que publique eventos en Kafka (o un mock de Kafka para pruebas).
 - Migraciones y datos de ejemplo, tests de integración con Testcontainers.
 - `docker-compose` para ejecutar la pila local (Postgres R2DBC, Kafka/Redpanda, Redis opcional).

 ## 3. Stack recomendado
 - Java 17+ (21 recomendable)
 - Spring Boot 3.x, Spring WebFlux
 - Spring Security (reactivo) — Resource Server + Authorization Server opcional
 - Spring Data R2DBC (Postgres R2DBC driver)
 - Reactor (Mono / Flux)
 - Kafka para eventos (consumer/producer reactivo)
 - Redis (cache, rate-limiting) opcional3
 - Testcontainers para pruebas de integración
 - OpenTelemetry + Micrometer → Prometheus/Grafana para observabilidad

 ---

 ## 4. Requisitos funcionales (resumen)
 1. Registro / Login / Refresh / Logout con JWT (RS256 recomendado) y rotación/revocación de refresh tokens.
 2. CRUD de productos, categorías y listings (el vendedor publica listings).
 3. Carrito y creación de órdenes desde el carrito.
 4. Reserva atómica de inventario (reserve / confirm / cancel).
 5. Pago simulado con estados (INITIATED, AUTHORIZED, CAPTURED, FAILED, REFUNDED).
 6. Envío (creación de shipment por servicio consumidor de eventos).
 7. Auditoría append-only y outbox para publicar eventos transaccionalmente.

 ## 5. Requisitos no funcionales
 - Arquitectura reactiva (no bloquear hilos en pipelines principales).
 - DB-per-service y transacciones reactivas (R2dbcTransactionManager) para writes locales.
 - Outbox pattern para garantizar publicación de eventos después de commit.
 - Idempotencia y keys de idempotencia para endpoints críticos.
 - Optimistic locking para `Inventory` (campo `version`) y operaciones de reserva.
 - Observabilidad: traces, metrics, logs estructurados.
 - Tests: unitarios, tests reactivos (StepVerifier) e integración (Testcontainers).

 ---

 ## 6. Modelo de dominio (resumen de entidades clave)
 - User: `id (UUID)`, `email`, `username`, `passwordHash`, `roles`, `verified`, timestamps.
 - Address: direcciones de usuario.
 - Product / Category / Listing: catálogo y publicaciones de vendedores.
 - Inventory: `listing_id` (único), `available`, `reserved`, `version` (@Version).
 - Cart / CartItem: snapshot de ítems para crear orden.
 - Order / OrderItem: estado de la orden, total, relación con pago y shipment.
 - Payment: status, amount, response provider, risk_score.
 - Shipment, Review, Coupon, AuditEvent, OutboxEvent.

 Notas: Evitar exponer entidades directamente en APIs — usar DTOs y proyecciones.

 ## 7. Relaciones y decisiones de diseño (breve)
 - `Listing` 1:1 `Inventory` — inventario crítico, operar con updates condicionales.
 - `Order` guarda snapshot de `OrderItem.product_snapshot` para consistencia histórica.
 - Indices sugeridos: `users(email) UNIQUE`, `listing(seller_id, status)`, `orders(buyer_id, created_at)`, `outbox(status, created_at)`.

 ---

 ## 8. Flujo crítico: creación de orden (happy-path)
 1. Cliente POST `/api/orders` con snapshot del carrito, `paymentMethodId`, `addressId` y opcional `coupon`.
 2. Validaciones: usuario autenticado, validez del cupón, cálculo de totales.
 3. Reserva de stock (operación atómica SQL): actualizar `available` y `reserved` solo si `available >= qty`.
 4. Persistir `Order` y `OrderItem` y crear `OutboxEvent(order.created)` en la misma transacción reactiva.
 5. Worker publica `order.created` en Kafka.
 6. `payment-service` procesa (async o sync). Si el pago es exitoso: `payment.CAPTURED`, `order.PAID`, publicar eventos. Si falla: publicar `order.CANCELLED` y liberar inventario (compensación).

 Recomendación: usar SAGA por coreografía (eventos) para escalabilidad; orquestador es una alternativa.

 ---

 ## 9. Endpoints principales (resumen)
 - Auth: `POST /api/auth/register`, `POST /api/auth/login`, `POST /api/auth/refresh`, `POST /api/auth/logout`.
 - Users: `GET /api/users/{id}`, `PUT /api/users/{id}`, `GET /api/users/me`.
 - Catalog: `GET /api/categories`, `GET /api/products`, `GET /api/products/{id}`.
 - Listings: `POST /api/listings`, `GET /api/listings`, `PATCH /api/listings/{id}`.
 - Inventory (internal): `POST /api/inventory/{listingId}/reserve`, `/confirm`, `/cancel`.
 - Cart: `GET /api/cart`, `POST /api/cart/items`, `DELETE /api/cart/items/{itemId}`.
 - Orders: `POST /api/orders`, `GET /api/orders/{id}`.
 - Payments: `POST /api/payments`, `GET /api/payments/{id}`.

 ---

 ## 10. Seguridad (resumen)
 - Token JWT RS256 con JWKs (Authorization Server recomendado) o validar clave RSA local para ejercicio.
 - Access tokens cortos, refresh tokens rotativos y almacenados (hash) para revocación.
 - Roles: `ADMIN`, `SELLER`, `BUYER`, `SUPPORT` y políticas ABAC para controles finos.
 - Inter-service: OAuth2 client_credentials o mTLS en producción.
 - Protecciones: rate-limit en endpoints de auth (Redis), bloqueo tras N intentos fallidos.

 ---

 ## 11. Messaging / Eventos
 - Topics sugeridos: `order.events`, `payment.events`, `inventory.events`, `listing.events`, `audit.events`.
 - Envelope recomendado: incluye `id`, `type`, `aggregateType`, `aggregateId`, `timestamp`, `payload`, `traceId`.

 Ejemplo de esquema JSON (resumido):
 ```json
 {
     "id": "uuid",
     "type": "order.created",
     "aggregateType": "order",
     "aggregateId": "uuid",
     "timestamp": "2025-11-21T12:34:56Z",
     "payload": { },
     "traceId": "..."
 }
 ```

 ---

 ## 12. Observabilidad y operaciones
 - Tracing: propagar traceId y instrumentar WebClient, Kafka y R2DBC.
 - Métricas: exportar `actuator/prometheus` con Micrometer.
 - Logs: JSON estructurado con `traceId`, `spanId`, `requestId`, `userId`.
 - Alertas: consumer lag, outbox backlog, tasa de fallos de pago, errores de reserva.

 ---

 ## 13. Testing (requisitos de evaluación)
 - Unit tests: JUnit5, Mockito.
 - Reactive tests: StepVerifier para Mono/Flux.
 - Integration: Testcontainers (Postgres R2DBC, Kafka/Redpanda).
 - Contract tests: opcional (Spring Cloud Contract / Pact) entre servicios críticos.

 ---

 ## 14. CI / CD (sugerencias)
 - GitHub Actions: build, pruebas unitarias, análisis estático, integration tests (Testcontainers), build de imagen.
 - CD: push image → deploy a staging con Helm; run smoke tests; canary/blue-green opcional.

 ---

 ## 15. Comandos útiles (run local)
 1. Compilar:
 ```bash
 ./mvnw -DskipTests package
 ```
 2. Ejecutar (modo desarrollo con `spring-boot:run`):
 ```bash
 ./mvnw spring-boot:run
 ```
 3. Tests:
 ```bash
 ./mvnw test
 ```
 4. Docker Compose (ejecutar pila local):
 ```bash
 docker-compose up --build
 ```

 Nota: revisar `src/main/resources/application.yml` para variables y perfiles.

 ---

 ## 16. Snippets importantes (orientación rápida)
 - Reserva atómica (SQL):
 ```sql
 UPDATE inventory
 SET available = available - :qty, reserved = reserved + :qty, version = version + 1
 WHERE listing_id = :listing_id AND available >= :qty AND version = :version;
 ```
 - Outbox table (ejemplo):
 ```sql
 CREATE TABLE outbox_event (
     id uuid PRIMARY KEY,
     aggregate_type text,
     aggregate_id uuid,
     event_type text,
     payload jsonb,
     status text DEFAULT 'PENDING',
     created_at timestamptz default now(),
     sent_at timestamptz
 );
 CREATE INDEX idx_outbox_status_createdat ON outbox_event(status, created_at);
 ```

 ---

 ## 17. Criterios de evaluación sugeridos
 - Seguridad y autenticación (25%)
 - Consistencia y resiliencia (20%)
 - Modelado y consultas (15%)
 - Correctitud reactiva (15%)
 - Tests & CI (10%)
 - Observability & DevOps (10%)
 - Calidad de código y arquitectura (5%)

 ---

 ## 18. Notas finales y siguientes pasos
 - Esta versión prioriza claridad y accionabilidad. Si quieres, puedo:
     - Generar un `README.md` con pasos concretos y ejemplos `curl`.
     - Crear un `docker-compose.yml` de ejemplo para la pila mínima.
     - Extraer esquemas SQL/DDL en archivos separados (`sql/`).

 Indícame cuál de esos siguientes pasos prefieres y lo implemento.
