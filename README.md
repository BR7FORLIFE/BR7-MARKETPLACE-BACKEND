# 🛒 E-Commerce Reactive API

Proyecto de e-commerce desarrollado con **Spring WebFlux**, enfocado en arquitectura limpia, escalabilidad y buenas prácticas modernas. Este proyecto tiene fines **educativos**, pero implementa conceptos usados en sistemas reales.

---

## 🚀 Tecnologías utilizadas

- ⚡ Spring WebFlux (programación reactiva)
- 🧠 Arquitectura Hexagonal (Ports & Adapters)
- 🐘 PostgreSQL (R2DBC) — Base de datos relacional reactiva
- ⚡ Redis — Cache
- 🔐 Spring Security + JWT — Autenticación y autorización
- ☁️ Despliegue en la nube
- 🐳 Docker (opcional)
- 📦 Maven

---

## 🏗️ Arquitectura

El proyecto sigue el patrón **Hexagonal (Clean Architecture)**:

application/
│
├── domain/ → lógica de negocio pura
├── application/ → casos de uso (use cases)
├── infrastructure/ → adapters (DB, controllers, etc)
└── config/ → configuración

---
### 🔄 Flujo de la aplicación
Controller → UseCase → Domain → Repository (Port) → Adapter (DB)

---
## ⚡ Programación Reactiva

Se utiliza:

- `Mono<T>` → 1 resultado
- `Flux<T>` → múltiples resultados

Ejemplo:

```java
Mono<Listing> findById(UUID id);
Flux<Listing> findAll()
```
## 🔐 Seguridad
- Autenticación con JWT
- Autorización basada en roles:
ADMIN
USER
- Protección de endpoints
⚡ Cache

## Uso de Redis para:
- Mejorar rendimiento
- Reducir carga en la base de datos


