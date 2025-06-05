# Práctica 3- Taller – Sistemas Distribuidos: Spring Boot + MySQL + seguridad
## Como poder ejecutar la práctica
### Primero clona el repositorio en el directorio que más desee
`git clone https://github.com/hgomezmartin/sistemas-distribuidos-workspace.git`

### Crea el Jar del Springboot
`./mvnw clean package -DskipTests`

### construye las imágenes Docker
`docker compose build`

### levanta las imagenes de Docker (MySql + Spring Boot)
#### Con log:
`docker compose up`
#### Sin log:
`docker compose up -d`

### Puedes comprovar el estado con (Opcional):
`docker compose ps`
`docker compose logs -f` 

## URLs
### Principal 
`http://localhost:8080/`

### Zonas Publicas
`http://localhost:8080/`
`http://localhost:8080/login`
`http://localhost:8080/register`
`http://localhost:8080/merch`
`http://localhost:8080/tickets`

### Zonas solo para usuarios logueados
`http://localhost:8080/profile`
`http://localhost:8080/cart`

### Zonas para el administrador (ROL_ADMIN) (no me ha dado tiempo a implementar todas, solo una)
`http://localhost:8080/users`


## Credenciales usadas en la DB (puedes registrarte para añadir mas usuarios):
### Usuario | Contraseña:
- admin    | admintop2025


## Parar y limpiar e proyecto
### Detiene contenedores: 
`docker compose down`
### Detiene contenedores + borra volumen
`docker compose down -v`


Hugo Gómez Martín - 2025

