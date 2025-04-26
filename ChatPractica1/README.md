# Práctica 1 – Sistemas Distribuidos: Sockets
Servidor multihilo + clientes que intercambian mensajes + diferentes comandos

## Como poder ejecutar la práctica
### clonamos el repo donde queramos
`https://github.com/hgomezmartin/sistemas-distribuidos-workspace.git`

### limpiamos y generamos .jar
`./mvnw clean package `

### podeos ejecutar el servidor y los clientes

- Para el servidor: `java -cp target/classes es.ubu.lsi.server.ChatServerImpl 1500`

- Para los diferentes clientes
`java -cp target/classes es.ubu.lsi.client.ChatClientImpl localhost 1500 usuarioA`
`java -cp target/classes es.ubu.lsi.client.ChatClientImpl localhost 1500 usuarioB`

NOTA: se pueden llamar como te de la gana

## Que comandos introducir
### Comando | efecto
- texto libre       | se mandan a todos los clientes
- `logout`          | Desconecta tu cliente
- `shutdown`        | Pide al servidor apagarse (sockets se cierran)
- `ban <usuario>`   | Banea y no muestra mensajes de ese usuario
- `unban <usuario>` | Lo sacamos de la lista de baneados

NOTA: da igual si los comandos los escribes en mayuscula o inuscula, no es key sensitive, lo unico que si es ensible a mayusculas y minusculas es el nombre o nick del usuario que quieras banear o desbanear.

Hugo Gómez Martín - 2025