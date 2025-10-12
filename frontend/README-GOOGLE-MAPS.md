# Integración Google Maps (Frontend)

Pasos para usar la API de Google Maps en este proyecto frontend:

1. Asegúrate de tener una API key de Google Maps con las APIs necesarias habilitadas (Maps JavaScript API y Places API).
2. En la terminal, desde `frontend/` exporta la variable de entorno:

```zsh
export GOOGLE_MAPS_API_KEY="YOUR_KEY_HERE"
```

3. Instala dependencias (si no está hecho):

```zsh
npm install
```

4. Para iniciar el dev server y que la llave se inyecte en `src/index.html` usa:

```zsh
npm run start:maps
```

El script `start:maps` ejecuta `node ./scripts/inject-google-key.js` (que reemplaza el placeholder en `src/index.html.template`) y luego lanza `ng serve`.

Notas de seguridad
- No comites tu API key al repositorio. Usa variables de entorno en tus máquinas o un secreto en CI.
- Para producción, considera inyectar la key en tiempo de despliegue y restringir la key por dominios en Google Cloud Console.

Componente de mapa
- Se añadió `MapComponent` en `src/app/map/map.component.ts` y está declarado como standalone e importado en `App`.
- El componente muestra un mapa y un campo de búsqueda (Places Autocomplete).
 - Se añadió `MapComponent` en `src/app/map/map.component.ts` y está declarado como standalone e importado en `App`.
 - El componente usa la Google Maps JavaScript API directamente (no depende de `@angular/google-maps`) y muestra un mapa y un campo de búsqueda (Places Autocomplete).

Problemas comunes
- Si `google` no está disponible cuando Angular inicializa, el componente hace polling breve para registrar Places Autocomplete.
- Si tu Angular CLI o entorno no soporta `standalone` imports (Angular v14+ required), actualiza Angular. Este proyecto ya usa Angular 20.

CI / Deployment (GitHub Actions example)
-------------------------------------
Para desplegar una demo o staging sin exponer la API key en el repo, guarda la clave como `GOOGLE_MAPS_API_KEY` en los Secrets del repositorio (Settings → Secrets → Actions) y añade un paso en tu workflow que inyecte la key antes de construir.

Ejemplo (fragmento de GitHub Actions):

```yaml
- name: Inject Google Maps key
	env:
		GOOGLE_MAPS_API_KEY: ${{ secrets.GOOGLE_MAPS_API_KEY }}
	run: |
		node frontend/scripts/inject-google-key.js

- name: Build frontend
	run: |
		cd frontend
		npm ci
		npm run build
```

Después del build puedes desplegar los artefactos (GitHub Pages, Netlify, Vercel, S3...). Asegúrate de restringir la API key por dominios en Google Cloud Console (por ejemplo el dominio de staging) para mayor seguridad.

Local (recordatorio rápido)
--------------------------
Cada desarrollador puede usar su propia key localmente:

```zsh
cd frontend
export GOOGLE_MAPS_API_KEY="TU_API_KEY_LOCAL"
npm install
npm run start:maps
```

