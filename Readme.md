# Huellas Vet — Ejecutar con Docker

Bienvenido a mi proyecto Huellas (Veterinaria). Podras ejecutar esta app sin instalar Java, Gradle, Payara ni
PostgreSQL. Solo necesitás **Docker Desktop** (Windows/Mac) o **Docker Engine + Compose** (para el caso de Linux).

## 0. Instalar Docker

Instale Docker antes de seguir con el paso 1. Docker Desktop o Docker Engine + Compose (para el caso de Linux)

## 1. Verofocacopm de la instalación de Docker

Verificá la instalación:

```bash
docker --version
docker compose version
```

## 2. Compilacion del proyecto

Generá el archivo `.war`:

```bash
./gradlew clean war
```

## 3. Iniciar los contenedores

Desde la carpeta del proyecto:

```bash
docker compose up -d
```

Se iniciarán:

| Servicio           | URL                                  |
|--------------------|--------------------------------------|
| PostgreSQL         | `localhost:5432`                     |
| Payara             | `http://localhost:8080/huellas-vet/` |
| pgAdmin (opcional) | `http://localhost:5050`              |

Para verificar que estén en ejecución:

```bash
docker compose ps
```

## 4. Acceder a la aplicación

```
http://localhost:8080/huellas-vet/
```

## 5. Usar pgAdmin (opcional)

Ingresá a `http://localhost:5050` con:

- **Usuario:** `admin@huellas.local`
- **Contraseña:** `admin`

Para acceder al servidor huellas_db utilizá la siguiente credencial:

- **Base de datos:** `huellas_db`
- **Contraseña:** `huellas_pass`

## 6. Actualizar la aplicación

Si por alguna razon ah modificado el código:

```bash
./gradlew clean war
docker compose restart payara
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
docker compose logs payara
```

**Si modificaste algo en el archivo `.env`**

```bash
docker compose down
docker compose up -d
```

**Puertos ocupados**

Verificá que ningún otro servicio esté utilizando los puertos `5432` o `8080`.

## Compatibilidad

`ConnectionManager` detecta automáticamente las variables de entorno (`DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER` y
`DB_PASSWORD`). Si no existen, utiliza `db.properties`, por lo que el mismo `.war` funciona tanto en Docker como en una
instalación local.# huellas-vet
