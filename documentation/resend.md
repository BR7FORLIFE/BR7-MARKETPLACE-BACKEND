## Definicion

La verificación de emails en backend con Resend implica generar un token único, enviarlo vía Resend API (usando el SDK) al usuario y validar ese token en una ruta específica. Se crea un usuario en estado "no verificado", se envía un correo con un enlace que incluye un JWT, y al hacer clic, el backend cambia el estado a "verificado". 

## Flujo de verificaciones de email
```txt
[1] POST /register
[2] Crear usuario (PENDING_VERIFICATION)
[3] Generar token seguro
[4] Guardar hash del token en DB
[5] Publicar evento async
[6] Worker envía email via Resend
[7] Usuario hace click en link
[8] GET /verify?token=...
[9] Validar token (atómico)
[10] Cambiar estado a ACTIVE
```

### Modelo de datos
id -> id de verificacion de email

userId -> identificador de usuario

tokenHash -> token para validar email

expiresAt -> datetime de expiracion del token

consumedAt -> datetime en que se consumio el token

### Endpoints a implementar

- /api/auth/verify-email            
- /api/auth/resend-verification
- /api/auth/verification-status

**Explicacion**

==== /api/auth/verify-email?token=TOKEN ===

Descripcion: Consumir el token y activar el usuario

Logica Interna: 

    1. Hashear el token recibido
    2. UPDATE token: 
        - token_hash coincide
        - no consumido
        - no expirado
    3. si no afecta filas -> token invalido
    4. si afecta 1 fila -> UPDATE user -> status ACTIVE
    5. commit

=== /api/auth/resend-verification ===

Description: permitir reenviar el correo si el usuario no lo recibio


=== /api/auth/verification-status ===

Descripcion: Saber si el usuario ya verifico el email (funcionamiento para frontend SPA)

