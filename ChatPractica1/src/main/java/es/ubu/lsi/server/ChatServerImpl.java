package es.ubu.lsi.server;

import es.ubu.lsi.common.ChatMessage;
import es.ubu.lsi.common.ChatMessage.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementacion del server del chat que puede manejar multiples clientes conectados
 * 
 * El server se ejecuta en el puerto 1500 por defecto y utiliza sockets para la comuniacion.
 * Reenvia (con broadcast) los mensajes recibidos a todos los clientes y gestiona la conexión 
 * y desconexion de cada cliente mediante hilos
 * 
 * @author Hugo Gómez Martín
 */
public class ChatServerImpl implements ChatServer{
	
	/** Puerto por defecto del server*/
	private static int DEFAULT_PORT = 1500;
	
	/** Variable para asignar IDs (Uso de esta variable en el futuro???) */
	private int clientId;
	
	/** Formato de fecha para los distintos registros en consola*/
	private SimpleDateFormat sdf;
	
	/** Puerto en el que se ejecuta el servidor*/
	private int port;
	
	/** Flag que indica si el server esta activo*/
	private boolean alive;
	
	
	/** Mapa donde almacenamos y asociamos el ID del cliente con su hilo de gestion*/
	private Map<Integer, ServerThreadForClient> clients;
	
	/**
	 * Constructor que inicializa el server en el puerto especificado
	 * 
	 * @param port (puerto de escucha)
	 */
	public ChatServerImpl (int port) {
		this.port = port;
		this.clients = new HashMap<>();
		this.sdf = new SimpleDateFormat("hh:mm:ss");
	}
	
	/**
	 * Clase interna (inner) que gestiona la comunicacion con un cliente, extiende Thread
	 * (Cada cliente se maneja en un hilo independiente)
	 * 
	 */
	public class ServerThreadForClient extends Thread {
		/** Identificador del cliente*/
        private int id;
        
        /** Socket para la conexion con el cliente */
        private Socket socket;
        
        /** Flujo de entrada que recibe mensajes del cliente */
        private ObjectInputStream in;
        
        /** Flujo de salida para enviar mensajes al cliente*/
        private ObjectOutputStream out;
        
        /** Nombre de usuario */
        private String username;
        
        /** Flag que indica si el hilo debe seguir ejecutandose */
        private boolean sigue = true;
        
        
        /**
         * Constructor que inicializa la conexion con el cliente (socket), crea los flujos
         * in y out y procesa el mensaje inicial para obterner el username
         * 
         * @param socket (socket asociado al cliente)
         */
		public ServerThreadForClient(Socket socket) {
			this.socket = socket;
			
			try {
				// Creamos primero el out para evitar bloqueos
				out = new ObjectOutputStream(socket.getOutputStream());
				in = new ObjectInputStream(socket.getInputStream());
				
				// Se espera que el primer mensaje contenga el username
				ChatMessage firstMsg = (ChatMessage) in.readObject();
				String content = firstMsg.getMessage();
				
				if(content.startsWith("username ")) {
					this.username = content.substring("username ".length());
                    // Asignamos un ID, por ejemplo usando hashCode 
                    this.id = username.hashCode();
                    
                 // Envía un mensaje de confirmación al cliente con su ID asignado
                    ChatMessage confirmation = new ChatMessage(
                            this.id,
                            MessageType.MESSAGE,
                            "Bienvenido, " + username + ". Tu ID es " + this.id
                    );
                    writeMsg(confirmation);
                    System.out.println(sdf.format(new Date()) + " Cliente conectado: " + username + " (ID=" + id + ")");
				}else {
					this.username = "ClienteSinNombre";
                    this.id = this.username.hashCode();
                    System.out.println("Cliente conectado sin username, se le asigna " + username + " (ID=" + id + ")");
				}
				
				
			} catch(IOException | ClassNotFoundException e) {
				System.err.println("Error al crear los flujos del cliente: " + e.getMessage());
                sigue = false;
				
			}
		}

		/**
		 * Método principal del hilo que lee los mensajes enviados por cliente 
		 *  y realiza la accion correspondiente en funcion del tipo de mensaje 
		 */
		@Override
        public void run() {
			while (sigue) {
				try {
					ChatMessage msg = (ChatMessage) in.readObject();
					if(msg.getType() == MessageType.LOGOUT) {
						System.out.println("El cliente "+ username + "ID=" + id + ") ha realizado LOGOUT");
						sigue = false;
					} else if(msg.getType() == MessageType.SHUTDOWN) {
						System.out.println("El cliente "+ username + "ID=" + id + ") ha realizado SHUTDOWN");
						sigue = false;
						shutdown();
					}else if(msg.getType() == MessageType.MESSAGE) {
						System.out.println(sdf.format(new Date()) + " [" + username + " (ID=" + id + ")]: " + msg.getMessage());
                        broadcast(msg);
					}else {
						System.out.println("Mensaje de tipo desconocido recibido: " + msg.getType());
					}
				}catch(IOException | ClassNotFoundException e) {
					System.err.println("Error en la comunicación con el cliente (ID=" + id + "): " + e.getMessage());
					sigue = false; //aqui termina el bucle en caso de error
                    //break;	
				}
			}
			
			remove(id);
			close();
            
        }

		/**
		 * Envía un mensaje al clienre mediante out (ObjectOutputStream)
		 * @param message (mensaje a enviar)
		 */
		public void writeMsg(ChatMessage message) {
			try {
				out.writeObject(message);
			} catch(IOException e) {
				System.err.println("Error al enviar mensaje al cliente (ID=" + id + "): " + e.getMessage());
			}
			
		}

		/**
		 * Cierra el socket y sus flujos in y out, finalizando así la conexion con el cliente
		 */
		public void close() {
			try {
				if (in != null) {in.close();}
				if (out != null) {out.close();}
				if (socket != null) {socket.close();}
				
			} catch (IOException e) {
				System.err.println("Error al cerrar recursos del cliente (ID=" + id + "): " + e.getMessage());
			}
		}
    }

	/**
	 * Arranca el sevidor, abre un socket, esperamos conexiones y crea un hilo para cada cliente
	 */
	@Override
	public void startup() {
		alive = true;
		System.out.println("Iniciando servidor en el puerto " + port +", Espere");
		
		try (ServerSocket serverSocket = new ServerSocket(port);){
			System.out.println("Servidor iniciado, esperando clientes...");
			
			while (alive) {
				Socket socket = serverSocket.accept();
				
				if (!alive) {break;}
				
				ServerThreadForClient clientThread = new ServerThreadForClient(socket);
				clientThread.start();
                // Una vez creado el hilo, se añade al mapa usando su id como clave
                clients.put(clientThread.id, clientThread);
				
			}
		} catch (IOException e) {
			System.err.println("Error en el servidor: " + e.getMessage());
		}finally {
            // Si salimos del bucle, apagamos el servidor
            shutdown();
        }
		
	}

	/**
	 * Apaga al servidor cerrando las conexiones de los cliente ademas de limpiear el mapa de ids
	 */
	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		alive = false;
		System.out.println("Apagando servidor, Espere");
		
		for (ServerThreadForClient client : clients.values()) {
			System.out.println("- Usuario" + client + "cerrado");
			client.close();
		}
		
		clients.clear();
		
		System.out.println("Servidor apagado");
		
	}

	/**
	 * Reenvia un mensaje a tods los clientes conectados
	 * 
	 * @param message (mensaje a reenviar)
	 */
	@Override
	public void broadcast(ChatMessage message) {
		for (ServerThreadForClient client : clients.values()) {
			client.writeMsg(message);
		}
		
	}

	/**
	 * Elimina un cluente del mapa de clientes activos por su ID
	 * 
	 * @param id (el id del cliente a eliminar)
	 */
	@Override
	public void remove(int id) {
		if (clients.remove(id) != null) {
            System.out.println("Cliente con id " + id + " ha sido removido.");
        } else {
            System.out.println("No se encontró el cliente con id " + id + ".");
        }
		
	}
	
	/**
	 * Método principal que crea y arranca el server
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ChatServerImpl server = new ChatServerImpl(DEFAULT_PORT);
		server.startup();
	}

}
