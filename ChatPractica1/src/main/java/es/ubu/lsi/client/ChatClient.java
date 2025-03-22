package es.ubu.lsi.client;
import es.ubu.lsi.common.*;

/*
 * Interfaz que define las operaciones basicas de un cliente del chat
 */
public interface ChatClient {
	/**
	 * Inicia el cliente y establece la conexion con el server
	 * 
	 * @return true (si la conexión se establece correctamente, false en caso contrario)
	 */
	boolean start();
	
	/**
	 * envia un mensaje al servidor
	 * 
	 * @param msg (El mensaje a enviar)
	 */
	void sendMessage(ChatMessage msg);
	
	/**
	 * Desconecta al cliente cerrando el socket
	 */
	void disconnect();

}
