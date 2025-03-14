package es.ubu.lsi.client;
import es.ubu.lsi.common.*;

public interface ChatClient {
	
	boolean start();
	
	void sendMessage(ChatMessage msg);
	
	void disconnect();

}
