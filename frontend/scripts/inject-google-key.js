/*
 Simple script to inject the Google Maps JS API script tag into frontend/src/index.html
 Usage: node ./scripts/inject-google-key.js <GOOGLE_MAPS_API_KEY>
 It will copy src/index.html.template -> src/index.html replacing {{GOOGLE_MAPS_SCRIPT}} placeholder.
*/
const fs = require('fs');
const path = require('path');

const key = process.env.GOOGLE_MAPS_API_KEY || process.argv[2];
if (!key) {
  console.error('ERROR: GOOGLE_MAPS_API_KEY not provided. Set env var or pass as arg.');
  process.exit(1);
}

const tmplPath = path.join(__dirname, '..', 'src', 'index.html.template');
const outPath = path.join(__dirname, '..', 'src', 'index.html');

if (!fs.existsSync(tmplPath)) {
  console.error('ERROR: template not found at', tmplPath);
  process.exit(1);
}

let content = fs.readFileSync(tmplPath, 'utf8');
const scriptTag = `<script async defer src="https://maps.googleapis.com/maps/api/js?key=${key}&libraries=places"></script>`;
content = content.replace(/{{GOOGLE_MAPS_SCRIPT}}/g, scriptTag);
fs.writeFileSync(outPath, content, 'utf8');
console.log('Wrote', outPath);
