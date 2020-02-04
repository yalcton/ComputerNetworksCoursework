/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

import common.*;

public class RMIServer extends UnicastRemoteObject implements RMIServerI
{

	private int totalMessages = -1;
	private int[] receivedMessages;

	public RMIServer() throws RemoteException {
		super();
	}


	public void receiveMessage(MessageInfo msg) throws RemoteException {

		// TO-DO: On receipt of first message, initialise the receive buffer
		// totalMessages is -1, which means we need to create our message buffer.

		if (totalMessages == -1)
		{
			totalMessages = msg.totalMessages;
			receivedMessages = new int[totalMessages+1];

			for(int k=1; k <= totalMessages; k++)
			{
				receivedMessages[k] = 0;  // setting all values in the array to 0 value.
			}
		}

		// TO-DO: Log receipt of the message

		receivedMessages[msg.messageNum] = 1;

		// TO-DO: If this is the last expected message, then identify
		//        any missing messages

		//we need to identify any messages that are still empty, i.e. still = 0.

		for(int k = 0; k < totalMessages; k++){    //DEBUGGING!!!!
				System.out.println("Message no. " + k + " is: " + receivedMessages[k]);
		}



		if (msg.messageNum == totalMessages)
		{
			for(int k = 1; k <= totalMessages; k++)
			{
				System.out.println("The missing message indexes are as follows: ");
				if(receivedMessages[k] == 0)
				{
					System.out.println("Message no. " + k);
				}
			}
			totalMessages = -1;
		}
	}


		public static void main(String[] args)
		{

			// TO-DO: Initialise Security Manager

			if (System.getSecurityManager() == null){
	        System.setSecurityManager(new SecurityManager());
	    }

			String name = "//boris/RMIServer";

			try
			{ 		// TO-DO: Instantiate the server class
				RMIServer rmis = new RMIServer();
				 rebindServer("//146.169.52.16/RMIServer", rmis);
			 } catch (Exception e) {
					System.err.println("SERVER error:");
					e.printStackTrace();
			}
			System.out.println("SERVER bound");


		}

	protected static void rebindServer(String serverURL, RMIServer server)
	{

			// TO-DO:

		try{
				Registry registry = LocateRegistry.createRegistry(1099);
				registry.rebind(serverURL, server);
		}
		catch (Exception e) {
			 System.err.println("SERVER error:");
			 e.printStackTrace();
	 }

	}

}
