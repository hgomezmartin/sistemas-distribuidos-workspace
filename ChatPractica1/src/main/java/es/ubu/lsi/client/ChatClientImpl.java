package es.ubu.lsi.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import es.ubu.lsi.common.ChatMessage;
import es.ubu.lsi.common.ChatMessage.MessageType;

public class ChatClientImpl implements ChatClient {
	
	private String server;
	private String username;
	private static int port = 1500;
	private boolean carryOn = true;
	private int id;
	
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	
	public ChatClientImpl(String server, int port, String username) {
		this.server = server;
		this.port = port;
		this.username = username;
	}
	
	public class ChatClientListener implements Runnable {
        @Override
        public void run() {
            try {
            	while (carryOn) {
            		Object obj = in.readObject();
            		if (obj instanceof ChatMessage) {
            			ChatMessage incomingMsg = (ChatMessage) obj;
            			System.out.println(incomingMsg.getId() + ": " + incomingMsg.getMessage());
            		}
            	}
            } catch (ClassNotFoundException | IOException e) {
            	System.err.println("Error en el hilo de escucha: " + e.getMessage());	
            }
        }
    }

	@Override
	public boolean start() {
		// TODO Auto-generated method stub
		try {
			socket = new Socket(server, port);
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
			
			ChatClientListener listener = new ChatClientListener();
			Thread threadListener = new Thread(listener);;
			threadListener.start();
			
			System.out.println("Conectado al servidor " + server + " en el puerto " + port + " con username " + username + " (id=" + id + ")");
			return true;
			
			
		} catch (IOException e) {
			System.err.println("Error al iniciar el cliente: " + e.getMessage());
            return false;
			
		}
	}

	@Override
	public void sendMessage(ChatMessage msg) {
		try {
			out.writeObject(msg);
			//out.flush();
		} catch (IOException e) {
			System.err.println("Error al enviar el mensaje: " + e.getMessage());
		}
		
	}

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
	    		String input = scanner.nextLine();
	    		
	    		if ("logout".equalsIgnoreCase(input)) {
	    			String msgText = cliente.username + " patrocina el mensaje: logout";
	    			ChatMessage logoutMsg = new ChatMessage(cliente.id, MessageType.LOGOUT, msgText);
	    			cliente.sendMessage(logoutMsg);
	    			
	    			cliente.carryOn=false;
	    			
	    		}else if("shutdown".equalsIgnoreCase(input)){
	    			String msgText = cliente.username + " patrocina el mensaje: shutdown";
	    			ChatMessage shutdownMsg = new ChatMessage(cliente.id, MessageType.SHUTDOWN, msgText);
	    			cliente.sendMessage(shutdownMsg);
	    			
	    			cliente.carryOn=false;
	    			
	    		}else {
	    			String msgText = cliente.username + " patrocina el mensaje: "+ input;
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
