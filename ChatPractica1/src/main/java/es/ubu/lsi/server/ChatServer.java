package es.ubu.lsi.server;
import es.ubu.lsi.common.*;
/**
 * Interfaz que define las operaciones basicas del servidor del chat
 */
public interface ChatServer {
	/**
	 * Arranca el server escuchando las conexiones de los clientes
	 */
	void startup();
	
	/**
	 * Apaga el servidor cerrando todas las conexiones
	 */
	void shutdown();
	
	/**
	 * Reenvía un mensaje a todos los clientes conectados
	 * 
	 * @param message (mensaje a reenviar)
	 */
	void broadcast(ChatMessage message);
	
	/**
	 * Elimina a un cluente de la lista de clientes
	 * 
	 * @param id (identificdor del cliente a eliminar)
	 */
	void remove(int id);
	
}
