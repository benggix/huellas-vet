# Huellas Vet — Ejecutar con Docker

Bienvenido a mi proyecto Huellas (Veterinaria). Podrás ejecutar esta app sin instalar Java, Gradle, Tomcat ni
PostgreSQL. Solo necesitás **Docker Desktop** (Windows/Mac) o **Docker Engine + Compose** (para el caso de Linux).

## 0. Instalar Docker

Instale Docker antes de seguir con el paso 1. Docker Desktop o Docker Engine + Compose (para el caso de Linux)

## 1. Verificación de la instalación de Docker

Verificá la instalación:

```bash
docker --version
docker compose version
```

## 2. Iniciar los contenedores

Desde la carpeta del proyecto:

```bash
docker compose up -d --build
```

No hace falta compilar nada a mano: el `Dockerfile` compila el proyecto con Gradle, genera el `.war` y lo despliega en
Tomcat. La primera vez puede tardar unos minutos porque descarga las dependencias.

Se iniciarán:

| Servicio           | URL                     |
|--------------------|-------------------------|
| PostgreSQL         | `localhost:5432`        |
| App (Tomcat)       | `http://localhost:8080` |
| pgAdmin (opcional) | `http://localhost:5050` |

pgAdmin **no se inicia por defecto**: es solo para desarrollo y hay que pedirlo con el perfil `dev`:

```bash
docker compose --profile dev up -d --build
```

Para verificar que estén en ejecución:

```bash
docker compose ps
```

## 3. Acceder a la aplicación

```
http://localhost:8080
```

## 4. Usar pgAdmin (opcional)

Ingresá a `http://localhost:5050` (requiere haber iniciado el perfil `dev`) con:

- **Usuario:** `admin@huellas.com`
- **Contraseña:** `admin`

El servidor **Huellas Vet DB** ya viene configurado. Cuando pida credenciales utilizá:

- **Usuario:** `huellas_user`
- **Base de datos:** `huellas_db`
- **Contraseña:** `huellas_pass`

Estos son los valores por defecto. Si los cambiaste en el archivo `.env`, usá los tuyos.

## 5. Panel de administración

Desde el panel se pueden ver los turnos reservados, las consultas de contacto y los suscriptores del newsletter que
cargan los clientes.

```
http://localhost:8080/admin/login.jsp
```

Las credenciales **no están en este documento por seguridad** (la contraseña se guarda hasheada con
PBKDF2-HMAC-SHA256 + salt en `src/main/resources/admin.properties`, no estan en texto plano).

## 6. Actualizar la aplicación

Si por alguna razón has modificado el código, volvé a construir y reiniciar solo el servicio de la app:

```bash
docker compose up -d --build app
```

## 7. Detener Docker

Para detener la ejecución

```bash
docker compose down
```

Para eliminar también los datos almacenados:

```bash
docker compose down -v
```

## Problemas comunes

**Si los cambios realizados en `schema.sql` no se aplican**

```bash
docker compose down -v
docker compose up -d --build
```

**Si la aplicación no inicia**

```bash
docker compose logs app
```

**Si modificaste algo en el archivo `.env`**

```bash
docker compose down
docker compose up -d
```

**Puertos ocupados**

Verificá que ningún otro servicio esté utilizando los puertos `5432`, `8080` (o `5050` si usás pgAdmin).

## Compatibilidad

La conexión a la base de datos se configura en `DbConfig`, y `ConnectionManager` administra el pool de conexiones
(HikariCP). El criterio es el siguiente:

- Si existe la variable de entorno `DB_HOST`, se usan las variables de entorno (`DB_HOST`, `DB_PORT`, `DB_NAME`,
  `DB_USER` y `DB_PASSWORD`). Las que no estén definidas toman un valor por defecto, y `db.properties` no se lee.
- Si `DB_HOST` no existe, se utiliza únicamente `db.properties` (`src/main/resources`).

De esta forma el mismo `.war` funciona tanto en Docker como en una instalación local.
