# 🔐 RBAC (Role-Based Access Control)

## 📌 ¿Qué es RBAC?

**RBAC (Role-Based Access Control)** es un modelo de autorización donde el acceso a recursos del sistema se controla según el **rol** asignado a un usuario.

En lugar de asignar permisos directamente a cada usuario, se agrupan permisos dentro de roles, y los usuarios reciben uno o varios roles.

---

## 🧠 Concepto Fundamental

RBAC responde a la pregunta:

> ¿Qué puede hacer este usuario en el sistema?

No decide quién eres (eso es autenticación), sino qué acciones puedes ejecutar.

---

## 🏗 Modelo Conceptual

La estructura básica de RBAC es:

User → Role → Permission


### Componentes:

- **User (Usuario)**  
  Persona o entidad que interactúa con el sistema.

- **Role (Rol)**  
  Conjunto de permisos que representan una función dentro del sistema.

- **Permission (Permiso)**  
  Acción específica que se puede ejecutar sobre un recurso.

---

## 🗄 Modelo de Base de Datos

En sistemas bien diseñados, RBAC se modela con las siguientes tablas:

### users
| Campo | Tipo |
|-------|------|
| id | UUID |
| username | VARCHAR |
| email | VARCHAR |

---

### roles
| Campo | Tipo |
|-------|------|
| id | UUID |
| name | VARCHAR (ADMIN, USER, SELLER) |

---

### permissions
| Campo | Tipo |
|-------|------|
| id | UUID |
| name | VARCHAR (CREATE_LISTING, DELETE_USER) |

---

### user_roles (tabla intermedia)
| Campo | Tipo |
|-------|------|
| user_id | UUID |
| role_id | UUID |

---

### role_permissions (tabla intermedia)
| Campo | Tipo |
|-------|------|
| role_id | UUID |
| permission_id | UUID |

---

## 🔄 ¿Cómo Funciona RBAC en una Request?

### 1️⃣ Autenticación
El usuario inicia sesión y obtiene un token (por ejemplo, JWT).

El token puede contener:
- userId
- roles

---

### 2️⃣ Autorización

Cuando el usuario hace una petición:

1. Se valida el token.
2. Se identifican los roles del usuario.
3. Se verifica si alguno de sus roles contiene el permiso requerido.
4. Si lo tiene → acceso permitido.
5. Si no lo tiene → acceso denegado.

---

## 🎯 Ejemplo Práctico

Supongamos que tenemos:

### Roles:
- ADMIN
- SELLER
- USER

### Permisos:
- CREATE_LISTING
- DELETE_USER
- VIEW_PROFILE

### Asignaciones:

| Role | Permisos |
|------|----------|
| ADMIN | Todos |
| SELLER | CREATE_LISTING |
| USER | VIEW_PROFILE |

---

### Escenario

Un usuario con rol `SELLER` intenta crear una publicación.

El sistema verifica:

¿El rol SELLER tiene permiso CREATE_LISTING?


✔ Sí → Permitido  
❌ No → Forbidden (403)

---

## 🛡 Implementación en Backend

RBAC puede implementarse en tres niveles:

---

### 🔹 Nivel 1: Capa de Seguridad (Framework)

Ejemplo conceptual:

Solo usuarios con rol SELLER pueden acceder a /listing/create


Esto bloquea el endpoint directamente.

---

### 🔹 Nivel 2: Capa de Aplicación

Dentro del caso de uso:

if (!authorizationService.hasPermission(userId, "CREATE_LISTING")) {
throw ForbiddenException
}


Esto es más robusto y flexible.

---

### 🔹 Nivel 3: Dominio (Protección adicional)

El dominio puede validar reglas críticas para evitar inconsistencias.

---

## 🚀 Ventajas de RBAC

✔ Fácil de entender  
✔ Escalable  
✔ Estandarizado  
✔ Reduce duplicación de permisos  
✔ Simplifica gestión administrativa  

---

## ⚠️ Limitaciones

RBAC no maneja bien:

- Reglas dinámicas (ej: solo editar tus propios recursos)
- Condiciones basadas en atributos
- Contexto (hora, país, estado del recurso)

Para eso se usa **ABAC (Attribute-Based Access Control)** o una combinación RBAC + validaciones de dominio.

---

## 🏆 Buenas Prácticas

- No hardcodear roles en múltiples lugares.
- Centralizar la lógica de autorización.
- Usar permisos granulares.
- No mezclar autorización con reglas de negocio.
- No almacenar demasiada lógica en el token si los permisos cambian frecuentemente.

---

## 📌 RBAC vs Autenticación

| Concepto | Pregunta que responde |
|----------|----------------------|
| Autenticación | ¿Quién eres? |
| Autorización (RBAC) | ¿Qué puedes hacer? |

---

## 🧩 RBAC en Arquitectura Hexagonal

En una arquitectura limpia, RBAC debería estar en un módulo separado:

authorization/
├── domain
├── application
├── infrastructure


El usuario (User) no debería contener lógica de roles directamente.

---

## 🔥 Conclusión

RBAC es un modelo de autorización basado en roles que permite:

- Controlar acceso de forma estructurada
- Simplificar administración de permisos
- Escalar el sistema sin asignar permisos individuales

Es ideal para sistemas donde las reglas de acceso dependen principalmente del rol del usuario.

---

**En resumen:**

RBAC define qué puede hacer un usuario según el rol que tiene dentro del sistema.
