package es.ubu.lsi.server;
import es.ubu.lsi.common.*;

public interface ChatServer {
	
	void startup();
	
	void shutdown();
	
	void broadcast(ChatMessage message);
	
	void remove(int id);
	
}
