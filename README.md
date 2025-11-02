# mono-repo

# Proyecto UrbanoFix

---Este documento describe los pasos necesarios para levantar el entorno de desarrollo local del backend de UrbanoFix.

El proyecto utiliza Spring Boot para la aplicación y PostgreSQL para la base de datos. Para facilitar la instalación, todo el entorno está "dockerizado" y se gestiona a través de docker-compose.---

# Requisitos previos

Antes de empezar, asegúrate de tener instaladas las siguientes herramientas en tu sistema:

1. Git: Para clonar el repositorio.
2. Java (JDK) 21: La aplicación está construida con Java 21. Puedes verificar tu versión con java --version.
3. Docker Desktop (o Docker Engine): Esta es la herramienta clave. La usaremos para levantar la base de datos PostgreSQL con PostGIS y la aplicación Spring Boot.

Descargar Docker Desktop: https://www.docker.com/products/docker-desktop/

# Configuración del Entorno

Sigue estos pasos para configurar el proyecto en tu máquina local.

1. Clonar el Repositorio
Abre tu terminal y clona el proyecto (si aún no lo has hecho):

--- git clone [URL-DE-TU-REPOSITORIO-GIT]
cd [nombre-del-repo]/backend ----

2. Configurar las Variables de Entorno (.env)
La aplicación utiliza un archivo .env para gestionar todas las configuraciones sensibles (credenciales de base de datos, API keys, etc.). Este archivo es privado, no se sube a GitHub.

**Crea tu archivo .env: En la raíz de la carpeta backend/, encontrarás una plantilla llamada .env.example. Haz una copia de este archivo y renómbrala a .env.** 

cp .env.example .env

**Edita tu archivo .env: Abre el nuevo archivo .env con tu editor de código. Verás que la mayoría de las variables (como la URL de la base de datos de Docker) ya están configuradas para desarrollo local.**

Solo necesitas rellenar los secretos reales que faltan:

--(las variables de SPRING_DATASOURCE_... ya están bien para Docker)

# --- Seguridad JWT ---
# (Genera tu propia clave secreta, larga y aleatoria)
JWT_SECRET=TU_CLAVE_SECRETA_PARA_JWT

# --- Configuración de Email (SMTP) ---
EMAIL_USERNAME=tu-correo@gmail.com
EMAIL_PASSWORD=tu-contraseña-de-aplicacion-de-email ----

# Cómo Levantar el Entorno (Docker Compose)
Este es el método principal para ejecutar todo el entorno (Backend + Base de Datos) con un solo comando.

1. Construir y Levantar: Estando en la raíz de tu proyecto (mono-repo/), ejecuta el siguiente comando:

--- docker-compose up --build ---

2. ¿Qué está pasando?

--- Docker descargará la imagen de postgis/postgis.

--- Ejecutará tu script db-init/init.sql para crear los ENUMs y activar las extensiones de la base de datos.

--- construirá tu aplicación Spring Boot (usando backend/Dockerfile). --

---Tu backend se conectará a la base de datos de Docker usando las credenciales de tu archivo .env.

---¡Tu backend estará corriendo en http://localhost:8081!

3. Comandos Útiles de Docker
Para detener el entorno: Presiona Ctrl + C en la terminal. O, desde otra terminal, ejecuta:
--- docker-compose down ---

5. Para reiniciar la base de datos (Borrar todos los datos): Si necesitas empezar con una base de datos limpia, ejecuta:
--- docker-compose down -v ---



