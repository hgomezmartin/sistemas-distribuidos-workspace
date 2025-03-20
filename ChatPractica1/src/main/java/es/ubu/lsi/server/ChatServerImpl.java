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
import java.util.concurrent.atomic.AtomicInteger;


public class ChatServerImpl implements ChatServer{
	
	private static int DEFAULT_PORT = 1500;
	private int clientId;
	private SimpleDateFormat sdf;
	private int port;
	private boolean alive;
	
	private AtomicInteger nextId = new AtomicInteger(1);
	
	private Map<Integer, ServerThreadForClient> clients;
	
	public ChatServerImpl (int port) {
		this.port = port;
		this.clients = new HashMap<>();
		this.sdf = new SimpleDateFormat("hh:mm:ss");
	}
	
	public class ServerThreadForClient extends Thread {
        private int id;
        private Socket socket;
        private ObjectInputStream in;
        private ObjectOutputStream out;
        private String username;
        private boolean sigue = true;
        
        

		public ServerThreadForClient(Socket socket) {
			this.socket = socket;
			
			try {
				out = new ObjectOutputStream(socket.getOutputStream());
				in = new ObjectInputStream(socket.getInputStream());
				
				ChatMessage firstMsg = (ChatMessage) in.readObject();
				String content = firstMsg.getMessage();
				
				if(content.startsWith("username ")) {
					this.username = content.substring("username ".length());
                    // Asignamos un ID, por ejemplo usando hashCode 
                    this.id = nextId.getAndIncrement();
                    
                 // Enviar un mensaje de confirmación al cliente con su ID asignado
                    ChatMessage confirmation = new ChatMessage(
                            this.id,
                            MessageType.MESSAGE,
                            "Bienvenido, " + username + ". Tu ID es " + this.id
                    );
                    writeMsg(confirmation);
                    System.out.println(sdf.format(new Date()) + " Cliente conectado: " + username + " (ID=" + id + ")");
				}else {
					this.username = "ClienteSinNombre";
                    this.id = nextId.getAndIncrement();
                    System.out.println("Cliente conectado sin username, se le asigna " + username + " (ID=" + id + ")");
				}
				
				
			} catch(IOException | ClassNotFoundException e) {
				System.err.println("Error al crear los flujos del cliente: " + e.getMessage());
                sigue = false;
				
			}
		}

		@Override
        public void run() {
			while (sigue) {
				try {
					ChatMessage msg = (ChatMessage) in.readObject();
					if(msg.getType() == MessageType.LOGOUT) {
						System.out.println("El cliente "+ username + " (ID=" + id + ") ha realizado LOGOUT");
						sigue = false;
					} else if(msg.getType() == MessageType.SHUTDOWN) {
						System.out.println("El cliente "+ username + " (ID=" + id + ") ha realizado SHUTDOWN");
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
					sigue = false; //aqui
                    //break;	
				}
			}
			
			remove(id);
			close();
            
        }

		public void writeMsg(ChatMessage message) {
			try {
				out.writeObject(message);
			} catch(IOException e) {
				System.err.println("Error al enviar mensaje al cliente (ID=" + id + "): " + e.getMessage());
			}
			
		}

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
			// TODO Auto-generated catch block
			System.err.println("Error en el servidor: " + e.getMessage());
		}finally {
            // Si salimos del bucle, apagamos el servidor
            shutdown();
        }
		
	}

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

	@Override
	public void broadcast(ChatMessage message) {
		for (ServerThreadForClient client : clients.values()) {
			client.writeMsg(message);
		}
		
	}

	@Override
	public void remove(int id) {
		if (clients.remove(id) != null) {
            System.out.println("Cliente con id " + id + " ha sido removido.");
        } else {
            System.out.println("No se encontró el cliente con id " + id + ".");
        }
		
	}
	
	public static void main(String[] args) {
		ChatServerImpl server = new ChatServerImpl(DEFAULT_PORT);
		server.startup();
	}

}
