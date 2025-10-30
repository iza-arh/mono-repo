/*
  PASO 1: Activar la extensión PostGIS
  (Soluciona: "type 'geography'/'geometry' does not exist")
*/
CREATE EXTENSION IF NOT EXISTS postgis;


/*
  PASO 2: Crear TODOS tus tipos ENUM
  (Soluciona: "type 'user_role'/'report_state'/'severity_t' does not exist")
*/
CREATE TYPE user_role AS ENUM (
  'ADMIN', 'USER', 'TECHNICIAN'
);

CREATE TYPE report_state AS ENUM (
  'REPORTED', 'VALIDATED', 'REJECTED', 'ASSIGNED', 
  'IN_TRANSIT', 'IN_PROGRESS', 'RESOLVED', 'NO_PROCEDE'
);

CREATE TYPE severity_t AS ENUM (
  'LOW', 'MEDIUM', 'HIGH', 'CRITICAL'
);