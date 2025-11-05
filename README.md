# mono-repo

# Proyecto UrbanoFix

En este proyecto creamos lo que es una aplicacion el cual va ayudar a los usarios a reportar diferente daños
de las calles, estructuras de partes de la cuidad en la cual el usuario tomara una foto y reportara desde su ubicacion gps
el daño y este creara un reporte.

---

##  Requisitos Previos (Para todo el proyecto)

Antes de empezar, asegúrate de tener instaladas las siguientes herramientas en tu sistema:

1.  **Git:** Para clonar el repositorio.
```bash
git clone --branch dev https://github.com/iza-arh/mono-repo.git mono-repo
```
2.  **Docker Desktop (o Docker Engine):** Para levantar el backend y la base de datos.
    * [Descargar Docker Desktop](https://www.docker.com/products/docker-desktop/)
3.  **Java (JDK) 21:** Para el backend. Puedes verificar tu versión con `java --version`.
4.  **Node.js (v18 o superior):** Para el frontend. Puedes verificar tu versión con `node -v`.
---

##  Configuración del Backend (API + Base de Datos)

El backend utiliza **Spring Boot** y **PostgreSQL (con PostGIS)**. Se ejecuta 100% dentro de Docker usando `docker-compose`.

### 1. Configurar las Variables de Entorno (`.env`)

El backend necesita un archivo `.env` para gestionar los secretos.

1.  **Navega a la carpeta del backend:**
```bash
cd mono-repo/backend
```
2.  **Poner el archivo `.env` en el backend que se compartio**
El archivo .env lo ponemos dentro del backend

### 2. Levantar el Backend

1. primero se dirige al backend
```bash
cd backend
```
2. Ejecutamos el comando de maven para construir el proyecto
```bash
./mvnw package
```
3. regresamos ala **raíz de tu proyecto (`mono-repo/`)**, ejecuta el siguiente comando:
##
```bash
docker-compose up --build
```

### Configuración del Frontend

## Primero antes de configurar el fronted tenemos que instalar las dependencias
1. @angular/material (Angular Material)
2. primeng (PrimeNG)
3. primeicons (PrimeNG Icons)
4. @auth0/auth0-angular (Autenticación)
5. tailwindcss (Tailwind CSS)
6. @angular/google-maps (Google Maps)

## Comando para instalar las dependecias
```bash
cd mono-repo/frontend
npm install
```

## Configuración el Entorno (Auth0)
1. Ve a frontend/src/app.config.ts/.
2. Edita el archivo app.config.ts 
3. Rellena tus credenciales de Auth0:

```bash
export const environment = {
  production: false,
  auth: {
    domain: 'dev-vze6sh41xjfo0djb.us.auth0.com',
    clientId: 'iXe2B0ROM3gqACZvH5HVRqdbIDiIjhLz',
  },
};
```
## Levantar el Frontend
Una vez instaladas las dependencias, inicia el servidor de desarrollo de Angular:
# (Estando en la carpeta 'frontend/')
```bash
ng serve
```

## Para crear y asignar lo que es el docker-compose.yml ##

1. Se creo la docker-compose.yml
2. Se agrego la base de datos
3. se agrego el backend
4. se agreggo el frontend
5. se agrego una indicacion el cual docke-compose tiene encontrar la variables de entorno

## Se tiene que ir ala carpeta raiz del proyecto ##
**En este caso se va mono-repo/**
## El comando que se tiene que usar para levantar en docker ##
```bash
docker-compose up --build
```










