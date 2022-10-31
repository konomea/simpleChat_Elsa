// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String loginID, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
    
    //sends server our login command automatically (without user typing it in)
    sendToServer("#login " + loginID);
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
	  if(message.charAt(0) == '#') {	//we have a command
		  String[] arguments = message.substring(1).split(" ");
		  String command = arguments[0];
		  switch(command) {
		  case "quit": quit(); break;
		  
		  /* TODO: if you log out then try to send a message it creates an error the ends the session*/
		  case "logoff": try {closeConnection();} 
		  	catch(IOException e){System.out.println("Could not close the connection.");} break;
		  
		  case "sethost": if(isConnected()) {System.out.println("Cannot change host when connected.");}
		  	else {setHost(arguments[1]); System.out.println("Host changed to " + arguments[1]);} 
		  	break;
		  
		  case "setport": if(isConnected()) {System.out.println("Cannot change port when connected.");}
		  	else {setPort(Integer.parseInt(arguments[1])); System.out.println("Port changed to " + arguments[1]);}
		  	break;
		  
		  case "login": if(isConnected()) {System.out.println("Already logged in.");}
		  	else {try{openConnection();} catch(IOException e){System.out.println("Could not login.");;}}
		  	break;
		  
		  case "gethost": System.out.println("Host is: " + getHost()); break;
		  
		  case "getport": System.out.println("Port is: " + getPort()); break;
		  
		  default: System.out.println("Not a valid command.");
		  }
	  }
	  
	  else { //regular message
		  try
		    {
		      sendToServer(message);
		    }
		    catch(IOException e)
		    {
		      clientUI.display
		        ("Could not send message to server.  Terminating client.");
		      quit();
		    }
	  }
    
  }
  
  public void connectionClosed() {
	  System.out.println("Connection has been closed.");
  }
  
  public void connectionException(Exception e) {
	  System.out.println("Server has stopped, client is going to be terminated.");
	  quit();
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
}
//End of ChatClient class
