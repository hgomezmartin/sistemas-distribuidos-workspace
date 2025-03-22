package es.ubu.lsi.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

import es.ubu.lsi.common.ChatMessage;
import es.ubu.lsi.common.ChatMessage.MessageType;

/**
 * Implementacion del cliente de chat
 * 
 * El cliente se conecta a un server con el puerto especificado, envia un mensaje
 * de registro con username y luego recibe un ID. Permite enviar mensajes y soporta
 * comandos como logout, shutdown, ban y unban
 * 
 * @author Hugo Gómez Martín
 */
public class ChatClientImpl implements ChatClient {
	
	/** Direccion del servidor */
	private String server;
	
	/** Nombre de usuario */
	private String username;
	
	/** Puerto que usa el servidor (por defecto 1500) */
	private static int port = 1500;
	
	/** Flag que indica si el cliente debe seguir funcionando */
	private boolean carryOn = true;
	
	/** ID asignado al cliente por el server */
	private int id;
	
	
	/** Socket para la conexion con el server */
	private Socket socket;
	
	/** Flujo de entrada que recibe mensajes del server*/
	private ObjectInputStream in;
	
	/** Flujo de salida para enviar mensajes al server*/
	private ObjectOutputStream out;
	
	
	/** Lista de baneos, donde se almacenan los IDs de usuarios baneados */
	private HashMap<Integer, String> banList = new HashMap<Integer, String>();
	
	
	/**
	 * Constructor que inicializael cliente con la direccion del sevidor, el puerto y username
	 * 
	 * @param server
	 * @param port
	 * @param username
	 */
	public ChatClientImpl(String server, int port, String username) {
		this.server = server;
		this.port = port;
		this.username = username;
	}
	
	/**
	 * Clase interna (inner) que se encarga de escuchar mensajes del server, implementa Runnable
	 */
	public class ChatClientListener implements Runnable {
        @Override
        public void run() {
            try {
            	while (carryOn) {
            		Object obj = in.readObject();
            		if (obj instanceof ChatMessage) {
            			ChatMessage chatMsg = (ChatMessage) obj;
            			// Si el usuario no esta baneado, se muestra el mensaje
            			if(!banList.containsKey(chatMsg.getId())) 
            			System.out.println(chatMsg.getId() + ": " + chatMsg.getMessage());
            		}
            	}
            } catch (ClassNotFoundException | IOException e) {
            	System.err.println("Error en el hilo de escucha: " + e.getMessage());	
            	carryOn = false;
            }
        }
    }
	
	/**
	 * Como su nombre indica, Inicializa la conexion al server, envia el mensaje de registro
	 * y arranca el hilo de escucha
	 * 
	 * @return true (si la conexion se establece correctamente, false al contrario)
	 */
	@Override
	public boolean start() {
		try {
			socket = new Socket(server, port);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			
			// enviamos un mensaje de registro (por ejemplo, indicando el username)
			ChatMessage registerMsg = new ChatMessage(0, MessageType.MESSAGE, "username " + username);
			sendMessage(registerMsg);

			// esperamso la respuesta del servidor con el ID asignado
			ChatMessage confirmation = (ChatMessage) in.readObject();
			this.id = confirmation.getId();
			
			//mantenemos la escuacha en 
			ChatClientListener listener = new ChatClientListener();
			Thread threadListener = new Thread(listener);;
			threadListener.start();
			
			System.out.println("Conectado al servidor " + server + " en el puerto " + port + " con username " + username + " (id=" + id + ")");
			return true;
			
			
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Error al iniciar el cliente: " + e.getMessage());
            return false;
			
		}
	}

	/**
	 * Envía un mensaje al server
	 * 
	 * @param msg (ensaje a enviar)
	 */
	@Override
	public void sendMessage(ChatMessage msg) {
		try {
			out.writeObject(msg);
		} catch (IOException e) {
			System.err.println("Error al enviar el mensaje: " + e.getMessage());
		}
		
	}

	/**
	 * Cierra el socket y sus flujos in y out, finalizando así la conexion con el server
	 */
	@Override
	public void disconnect() {
		try {
			carryOn = false;
			
			if (in != null) {in.close();}
			if (out != null) {out.close();}
			if (socket != null) {socket.close();}
			System.out.println("Desconectado del servidor.");
		} catch (IOException e) {
			System.err.println("Error al desconectar: " + e.getMessage());
		}
		
	}
	
	/**
	 * Método principal que inicia el cliente y procesa los comandos de entrada
	 * 
	 * @param args (Argumentos de linea de comandos, que basicamente son ~> server port username)
	 */
	public static void main(String[] args) {
		
		if (args.length < 3) {
            System.out.println("Uso: java es.ubu.lsi.client.ChatClientImpl <server> <port> <username>");
            return;
        }
		
		String server = args[0];
	    port = Integer.parseInt(args[1]);
	    String username = args[2];
	    
	    ChatClientImpl cliente = new ChatClientImpl(server, port, username);
	    
	    if (!cliente.start()) {
	    	System.err.println("No se pudo iniciar el cliente.");
            return;
	    } else {
	    	Scanner scanner = new Scanner(System.in);
	    	
	    	while (cliente.carryOn) {
	    		String command = scanner.next().toLowerCase();
	            String arguments = scanner.nextLine().trim();
	    		
	    		if ("logout".equalsIgnoreCase(command)) {
	    			String msgText = cliente.username + " patrocina el mensaje: logout";
	    			ChatMessage logoutMsg = new ChatMessage(cliente.id, MessageType.LOGOUT, msgText);
	    			cliente.sendMessage(logoutMsg);
	    			
	    			cliente.carryOn=false;
	    			
	    		}else if("shutdown".equalsIgnoreCase(command)){
	    			String msgText = cliente.username + " patrocina el mensaje: shutdown";
	    			ChatMessage shutdownMsg = new ChatMessage(cliente.id, MessageType.SHUTDOWN, msgText);
	    			cliente.sendMessage(shutdownMsg);
	    			
	    			cliente.carryOn=false;
	    			
	    		}else if("ban".equals(command)){
	    			if(!arguments.isEmpty()) {
	    				String userToBan = arguments;
	    				int userToBanId = userToBan.hashCode();
	    				
	    				// Verificamos que el usuario no se esté baneando a sí mismo
	    		        if (cliente.id == userToBanId) {
	    		            System.out.println("No puedes banearte a ti mismo.");
	    		        } else {
	    		            cliente.banList.put(userToBanId, userToBan);
	    		            
	    		            String banMsg = cliente.username + " ha baneado a " + userToBan;
	    		            ChatMessage banMessage = new ChatMessage(cliente.id, MessageType.MESSAGE, banMsg);
	    		            cliente.sendMessage(banMessage);
	    		        }
	    			}else {
	    				System.out.println("Te falta el argumento. Ej de uso: ban <usuario>");
	    			}
	    			
	    		}else if("unban".equals(command)){
	    			if(!arguments.isEmpty()) {
	    				String userToUnban = arguments;
	    				int userToUnbanId = userToUnban.hashCode();
	    				cliente.banList.remove(userToUnbanId, userToUnban);
	    				
	    				String unbanMsg = cliente.username + " ha desbaneado a " + userToUnban;
	    				ChatMessage unbanMessage = new ChatMessage(cliente.id, MessageType.MESSAGE, unbanMsg);
	                    cliente.sendMessage(unbanMessage);
	    			}else {
	    				System.out.println("Te falta el argumento. Ej de uso: ban <usuario>");
	    			}
	    			
	    		}else {
	    			String msgText = cliente.username + " patrocina el mensaje: "+ command + " " + arguments;
	    			ChatMessage message = new ChatMessage(cliente.id, MessageType.MESSAGE, msgText);
	    			cliente.sendMessage(message);
	    		}
	    	}
	    	scanner.close();

	        // Finalmente, desconectamos
	        cliente.disconnect();
		}
	}
}
