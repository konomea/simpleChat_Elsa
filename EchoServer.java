// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.IOException;
import common.ChatIF;

import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  ChatIF serverUI;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port, ChatIF serverUI) {
	  super(port);
	  this.serverUI = serverUI;
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client)
  {
	  if(msg.toString().charAt(0) == '#') {
		  String[] arguments = msg.toString().split(" ");
		  if(arguments[0].equals("#login")) {
			  if(client.getInfo("loginID") == null) {
				  client.setInfo("loginID", arguments[1]);
			  }
			  else {
				  try{
					  client.sendToClient("You cannot set another loginID.");
					  client.close();
				  }
				  catch(IOException e){}
			  }
		  }
		  System.out.println("Message received: #login " + arguments[1] + " from null");
		  clientConnected(client);
	  }
	  else {
		  System.out.println("Message received: " + msg + " from " + client.getInfo("loginID"));
		  this.sendToAllClients(client.getInfo("loginID")+ "> " + msg);
	  }
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  protected void clientConnected(ConnectionToClient client) {
	  
	  if(client.getInfo("loginID") != null) {
		  String s = client.getInfo("loginID") + " has logged on.";
		  sendToAllClients(s);
		  System.out.println(s);
	  }
	  else {
		  System.out.println("A new client has connected to the server.");
	  }
  }
  
  
  /**
 * Called in ConnectionToClient for each client thread.
 * Prints a message that 'loginID' has disconnected.
 */
synchronized protected void clientDisconnected(ConnectionToClient client) {
	  String s = client.getInfo("loginID") + " has disconnected.";
	  sendToAllClients(s);
	  System.out.println(s);
   }
  
	protected void handleMessageFromServerConsole(String message) {
		if(message.charAt(0)=='#') {
			String[] arguments = message.substring(1).split(" ");
			String command = arguments[0];
			
			switch(command) {
			case "quit": stopListening(); try{close();} // terminates the server
				catch(IOException e) {System.exit(1);}  //abnormal exit
				System.exit(0); //normal exit
				break;
			
			case "stop":  stopListening(); break; //doesn't terminate, just stops listening
			
			case "close": stopListening(); // stops listening and closes all connections but doesn't terminate
			try {close();} catch(IOException e) {} break;
			
			case "setport": if(getNumberOfClients()==0 && ! isListening()) {super.setPort(Integer.parseInt(arguments[1]));}
			else {System.out.println("Cannot change port while server is connected.");}
				break;
			
			case "start": if(isListening()) {System.out.println("Already listening.");}
				else {try{listen();} catch(IOException e){System.out.println("Error occured when trying to listen.");}}
				break;
			
			case "getport": System.out.println("Port is "+getPort()); break;
			
			default: System.out.println("Not a valid command.");
			}
		}
		else { 	// message is not a command
            this.sendToAllClients("SERVER MESSAGE> "+message);
            serverUI.display(message);
        }
	}
	
	}
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */


//End of EchoServer class
