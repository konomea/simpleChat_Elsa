import java.util.Scanner;
import common.ChatIF;

public class ServerConsole implements ChatIF {

	EchoServer server;
	Scanner fromConsole; 
	
	public ServerConsole(int port) 
	  {
		server = new EchoServer(port, this);
	    fromConsole = new Scanner(System.in); 
	  }
	
	public void accept() {
	    try{
	      String message;

	      while (true) {
	        message = fromConsole.nextLine();
		    server.handleMessageFromServerConsole(message);
	      }
	    } 
	    catch (Exception ex) {
	      System.out.println
	        ("Unexpected error while reading from console!");
	    }
	  }

	
	@Override
	public void display(String message) {
		if(message.charAt(0) == '#') { //message is a command, don't display it
			return;
		}
		System.out.println("SERVER MESSAGE> " + message);
	}
	
	  public static void main(String[] args)
	  {
	    int port = 0; //Port to listen on

	    try
	    {
	      port = Integer.parseInt(args[0]); //Get port from command line
	    }
	    catch(NumberFormatException e){
	    	port = EchoServer.DEFAULT_PORT;
	    }
	    catch(ArrayIndexOutOfBoundsException f){
	    	port = EchoServer.DEFAULT_PORT;
	    }
	    ServerConsole chat= new ServerConsole(port);

	    try 
	    {
	      chat.server.listen(); //Start listening for connections
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println("ERROR - Could not listen for clients!");
	    }
	    chat.accept();  //Wait for console data
	  }

}
