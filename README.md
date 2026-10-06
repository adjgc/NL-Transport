# NL Transport

**NL Transport** es una aplicación web concebida para la gestión y administración de clientes y cargas. La plataforma permite realizar un seguimiento detallado del estatus de los envíos y visualizar la ubicación actual de la flota/cargas en tiempo real mediante un mapa interactivo.

---

## 🚀 Tecnologías Utilizadas

- **Backend:** Java 25, Spring Boot, Maven
- **Base de Datos:** MySQL 8
- **Frontend & Mapas:** HTML5, CSS3, JavaScript, Leaflet.js, OpenStreetMap
- **Contenedorización:** Docker, Docker Compose (Multi-stage build con usuario sin privilegios y sistema de archivos de solo lectura)
- **Pruebas:** JUnit 5

---

## 🛠️ Requisitos Previos

- [Docker](https://www.docker.com/) y [Docker Compose](https://docs.docker.com/compose/)
- *(Opcional para desarrollo local sin Docker)* JDK 25 y Maven

---

## 💻 Despliegue Local

### Opción 1: Con Docker Compose (Recomendado)

1. **Clonar el repositorio y levantar los servicios:**
   Desde la raíz del proyecto, ejecuta el siguiente comando para compilar e iniciar los contenedores de la aplicación y la base de datos MySQL:

   ```powershell
   docker compose up --build -d
   ```

2. **Acceso a la aplicación:**
   Abre tu navegador e ingresa a [http://localhost:8080](http://localhost:8080). La aplicación creará automáticamente la estructura de tablas y cargará datos de prueba iniciales si la base de datos se encuentra vacía.

3. **Variables de Entorno y Configuración de Puertos:**
   - En entorno local se usan credenciales predeterminadas. Si necesitas modificarlas, copia el archivo `.env.example` a `.env` y asigna tus propios valores.
   - Si los puertos por defecto (`8080` para la app o `3306` para MySQL) están ocupados, define variables en `.env`:
     ```env
     APP_PUBLISHED_PORT=8081
     MYSQL_PUBLISHED_PORT=3307
     ```

4. **Comandos Útiles:**
   - **Ver logs en tiempo real:** `docker compose logs -f app`
   - **Detener los servicios (manteniendo datos):** `docker compose down`
   - **Detener los servicios y eliminar la BD (volúmenes):** `docker compose down -v`

---

### Opción 2: Ejecución Local sin Contenedor de Aplicación

1. **Iniciar únicamente la base de datos MySQL:**
   ```powershell
   docker compose up -d mysql
   ```

2. **Ejecutar la aplicación Spring Boot (Requiere Java 25):**
   Navega a la carpeta `transport` y arranca el servicio:
   ```powershell
   cd transport
   .\mvnw.cmd spring-boot:run
   ```
   *Nota: Se requiere conexión a internet activa para cargar el mapa base de OpenStreetMap vía Leaflet.*

---

## 🧪 Depuración y Pruebas

Para validar la compilación y ejecutar el conjunto de pruebas unitarias desde la raíz del repositorio:

```powershell
.\transport\mvnw.cmd -f .\transport\pom.xml clean test
```

O desde la carpeta del proyecto Spring Boot:

```powershell
cd transport
.\mvnw.cmd clean test
```

*Al importar en un IDE (como IntelliJ IDEA), abre la carpeta `transport/pom.xml` como proyecto Maven.*

---

## ⚙️ Integración continua con GitHub Actions

El workflow [`CI a Producción`](.github/workflows/ci.yml) se ejecuta en pull requests dirigidos a `main` y en cada push a esa rama. Cada ejecución se identifica como **CI a Producción - @github.actor** y realiza estas validaciones:

1. Compila el código Java.
2. Ejecuta los tests.
3. Valida la configuración de Docker Compose, descarga la imagen de MySQL y construye la imagen de la aplicación.
4. Si alguna validación falla, publica el detalle en el resumen de la ejecución y marca el workflow como fallido.

Para bloquear la integración de cambios con errores, configura una regla de protección para `main` en GitHub que requiera el check **Validar código**, **Validar tests**, **Validar imágenes Docker Compose** y **Notificar resultado de CI**. Las notificaciones personales de GitHub dependen de la configuración de watch/notificaciones de cada usuario.

---

## ☁️ Despliegue en la Nube

Este proyecto está optimizado y preparado para ser desplegado en una instancia de **AWS EC2**, aprovechando la arquitectura basada en contenedores de Docker para garantizar la consistencia entre el entorno de desarrollo y producción.

---

Desarrollado con 🤍 por [ADJGC](https://www.linkedin.com/in/adjgc)
