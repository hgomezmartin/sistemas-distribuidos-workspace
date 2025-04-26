# Práctica 2 – Sistemas Distribuidos: Spring Boot + Flask + MySQ(Docker-Compose)

## Como poder ejecutar la práctica

### Primero clona el repositorio en el directorio que más desee
git clone https://github.com/hgomezmartin/sistemas-distribuidos-workspace.git

### Crea el Jar del Springboot
`./mvnw clean package -DskipTests`

### construye las imágenes Docker
`docker compose build`

### levanta las imagenes de Docker (MySql + Spring Boot + Flask)
#### Con log:
`docker compose up`
#### Sin log:
`docker compose up -d`

### Puedes comprovar el estado con (Opcional):
`docker compose ps`
`docker compose logs -f` 

## URLs principales

### Home + login:
`http://localhost:8080/`

### Pantalla para lanzar manualmente cualquier endpoint de Flask
`http://localhost:8080/api-demo`

## Credenciales usadas en la DB:
### Usuario | Contraseña:
- Peter    | pass123
- Celio    | celio123
- Fernando | torres2009
- Rodolfo  | rodolfo123

## Endpoints de la api flask:
### Ruta | Tipo de prueba
- `/ping`                      | Comunicación
- `/api/file/file1.txt`        | Ok fichero
- `/api/file/noexite.txt`      | FileNotFound
- `/api/file/confidential.txt` | PermissionError (simulado)
- `/api/db/users`              | Ok BD
- `/api/db/force-error`        | Error SQL
- `/api/pokemon/pikachu`       | API externa ok
- `/api/pokemon/iniesta`       | API externa error 404
- `/api/timeout`               | Provoca timeor (RestTemplate corta a 5s)

NOTA: No obstante, si tienes alguna duda, en la carpeta api-frontend-capturas hay capturas para ver como se pueden usar estos endpoits en el frontend. No obtante, tambien puedes probarlo con Postman, donde en la carpeta del proyecto llamada Postman puedes comprobar como se realizan y que devuelven.

## Parar y limpiar e proyecto
### Detiene contenedores: 
`docker compose down`
### Detiene contenedores + borra volumen
`docker compose down -v`


Hugo Gómez Martín - 2025

