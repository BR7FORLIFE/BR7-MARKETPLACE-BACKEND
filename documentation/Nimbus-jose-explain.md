
# Nimbus JOSE + JWT — Dependencia

## 1. ¿Qué es Nimbus JOSE + JWT?
Nimbus JOSE + JWT es una librería Java utilizada para manejar:
- **JOSE** (JSON Object Signing and Encryption)
- **JWT** (JSON Web Tokens)

Es una de las más completas del ecosistema Java y es la que usan internamente Spring Security y Spring Authorization Server para trabajar con JWT, JWS, JWE y JWK.

**Implementa los estándares:**
- JWS: Tokens firmados
- JWE: Tokens cifrados
- JWK: Claves en formato JSON
- JWK Set: Conjuntos de claves
- JWA: Algoritmos de firma y cifrado
- JWT: Tokens con claims

---

## 2. ¿Qué modela Nimbus?
Modela todos los conceptos relacionados a JWT en seguridad moderna:

### JWS — JSON Web Signature
Tokens firmados con algoritmos:
- HS256 / HS384 / HS512
- RS256 / RS384 / RS512
- ES256 / ES384 / ES512

### JWE — JSON Web Encryption
Tokens cifrados usando:
- AES
- RSA-OAEP
- Password-based encryption

### JWK — JSON Web Key
Claves públicas/privadas en JSON:
- `kty` (tipo de clave)
- `alg` (algoritmo)
- `use` (firma o cifrado)
- `kid` (ID de la clave)

### Rotación de claves con JWK Set
Permite sincronizar claves desde:
- Keycloak
- Auth0
- Okta
- AWS Cognito
- Spring Authorization Server

---

## 3. Usos en microservicios / WebFlux

### Validación manual de JWT
Ideal cuando **no** usas Spring OAuth2 completo:
```java
SignedJWT signedJWT = SignedJWT.parse(jwt);
JWSVerifier verifier = new MACVerifier(secret);
boolean valid = signedJWT.verify(verifier);
```

### Lectura de claims
```java
String subject = signedJWT.getJWTClaimsSet().getSubject();
```

### Tokens con RSA
```java
JWSVerifier verifier = new RSASSAVerifier(publicKey);
```

### Manejo de JWKs (rotación automática de claves)
```java
JWKSet jwkSet = JWKSet.load(new URL("https://auth-server.com/.well-known/jwks.json"));
```

### Integración con WebFlux
Todas sus APIs son puras y no bloqueantes, ideales para un `JwtService` reactivo.

---

## 4. ¿Por qué usar Nimbus en vez de JJWT?

| Librería | Para qué sirve | Ideal |
|----------|----------------|-------|
| JJWT     | Simple, práctico, perfecto para HS256 | Apps MVC, tokens simples |
| Nimbus   | Completo soporte JOSE/JWK/JWS/JWE     | OAuth2, OIDC, microservicios, Keycloak |

Nimbus es la elección estándar cuando trabajas con:
- Microservicios distribuidos
- API Gateways
- Firmas RSA / EC
- Validación desde Authorization Servers externos

---

## 5. Ejemplo completo de validación con Nimbus
```java
public JWTClaimsSet validateToken(String token) {
    SignedJWT signedJWT = SignedJWT.parse(token);
    JWSVerifier verifier = new MACVerifier(secret);
    if (!signedJWT.verify(verifier)) {
        throw new RuntimeException("Invalid signature");
    }
    return signedJWT.getJWTClaimsSet();
}
```

---

## 6. Resumen
Nimbus JOSE + JWT es la librería estándar en Java para manejar tokens JWT firmados o cifrados, validarlos, trabajar con JWK sets y comunicarse con servidores OAuth2/OIDC en arquitecturas de microservicios.