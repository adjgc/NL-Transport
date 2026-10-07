# NL Transport

**NL Transport** es una aplicación web concebida para la gestión y administración de clientes y cargas. La plataforma permite realizar un seguimiento detallado del estatus de los envíos y visualizar la ubicación actual de la flota/cargas en tiempo real mediante un mapa interactivo.

---

## 🚀 Tecnologías Utilizadas

- **Backend:** Java 25, Spring Boot, Maven
- **Base de Datos:** PostgreSQL 17
- **Frontend & Mapas:** HTML5, CSS3, JavaScript, Leaflet.js, OpenStreetMap
- **Contenedorización:** Docker, Docker Compose (Multi-stage build con usuario sin privilegios y sistema de archivos de solo lectura)
- **Pruebas:** JUnit 5

---

## 🛠️ Requisitos Previos

- [Docker](https://www.docker.com/) y [Docker Compose](https://docs.docker.com/compose/)
- *(Opcional para desarrollo local sin Docker)* JDK 25 y Maven

---

## 💻 Despliegue Local

1. **Clonar el repositorio y levantar los servicios:**
   Desde la raíz del proyecto, ejecuta el siguiente comando para compilar e iniciar los contenedores de la aplicación y PostgreSQL 17:

   ```powershell
   docker compose --file compose.yml up --build -d
   ```

2. **Acceso a la aplicación:**
   Abre tu navegador e ingresa a [http://localhost:8080](http://localhost:8080). La aplicación creará automáticamente la estructura de tablas y cargará datos de prueba iniciales si la base de datos se encuentra vacía.

3. **Variables de Entorno y Configuración de Puertos:**
   - En entorno local se usan credenciales predeterminadas. Si necesitas modificarlas, copia el archivo `.env.example` a `.env` y asigna tus propios valores.
   - Si los puertos por defecto (`8080` para la app o `5432` para PostgreSQL) están ocupados, define variables en `.env`:
     ```env
     APP_PUBLISHED_PORT=8081
     POSTGRES_PUBLISHED_PORT=5433
     ```

4. **Comandos Útiles:**
   - **Ver logs en tiempo real:** `docker compose --file compose.yml logs -f app`
   - **Detener los servicios (manteniendo datos):** `docker compose --file compose.yml down`
   - **Detener los servicios y eliminar la BD (volúmenes):** `docker compose --file compose.yml down -v`

---

## ⚙️ Integración continua con GitHub Actions

El workflow [`CI a Producción`](.github/workflows/ci_cd.yml) se ejecuta en cada push a `main`. Cada ejecución se identifica como **CI a Producción - @github.actor** y sigue este proceso:

1. Compila el código Java.
2. Ejecuta los tests.
3. Valida la configuración de Docker Compose, descarga la imagen de PostgreSQL 17 y construye la imagen de la aplicación.
4. Si las validaciones pasan, publica `adjgc/nl_transport:latest` y una etiqueta inmutable con el SHA del commit en Docker Hub.
5. Solicita a Render desplegar la imagen `latest` mediante el Deploy Hook del Web Service.
6. Publica el resultado de cada job en el resumen y marca el workflow como fallido si una validación, publicación o solicitud de despliegue falla.

Configura estos secretos en **Settings > Secrets and variables > Actions** del repositorio:

| Secreto | Contenido |
| --- | --- |
| `DOCKERHUB_USERNAME` | Usuario de Docker Hub con permiso de escritura en `adjgc/nl_transport` |
| `DOCKERHUB_TOKEN` | PAT de Docker Hub con permiso de lectura y escritura |
| `RENDER_DEPLOY_HOOK` | URL del Deploy Hook del Web Service de Render |

---

## ☁️ Despliegue en Render

Render despliega la aplicación como un **Web Service** desde la imagen `docker.io/adjgc/nl_transport:latest` y proporciona PostgreSQL administrado. Render no ejecuta este `compose.yml` como una aplicación multi-contenedor; el Compose se usa para desarrollo local y para validar la imagen en CI. Configura el Web Service para usar esa imagen, agrega un Deploy Hook y guarda su URL en el secreto `RENDER_DEPLOY_HOOK`. Si la imagen de Docker Hub es privada, configura también en Render credenciales de lectura del repositorio.

1. Crea una base de datos PostgreSQL 17 en Render y un Web Service con **Existing Image** configurado como `docker.io/adjgc/nl_transport:latest`. Si el repositorio de Docker Hub es privado, agrega credenciales de lectura para que Render pueda descargar la imagen.
2. En la base de datos de Render, copia los datos de conexión **internos** (host, puerto, nombre de base, usuario y contraseña) al entorno del Web Service:

   | Variable | Valor |
   | --- | --- |
   | `PGHOST` | Host interno de la base de datos Render |
   | `PGPORT` | Puerto de la base de datos Render |
   | `PGDATABASE` | Nombre de la base de datos Render |
   | `PGUSER` | Usuario de la base de datos Render |
   | `PGPASSWORD` | Contraseña de la base de datos Render |

   Spring Boot arma la conexión PostgreSQL con estas variables y usa automáticamente el puerto `PORT` que Render define para el Web Service. No uses la URL externa de la base desde el servicio alojado en Render.
3. Despliega la aplicación. Hibernate crea o actualiza el esquema al arrancar. Los datos locales de MySQL no se migran automáticamente a PostgreSQL; realiza una migración explícita si necesitas conservarlos.

---

Desarrollado con 🤍 por [ADJGC](https://www.linkedin.com/in/adjgc)
