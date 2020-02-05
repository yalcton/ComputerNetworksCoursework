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

		if(msg.messageNum == 0)
		{
			totalMessages = 0;
			receivedMessages = new int[msg.totalMessages];
		}
		// make sure total messages incremented every time this is called.
		totalMessages++;

		// buffer the messages
		receivedMessages[msg.messageNum] = 1;

		// for(int k = 0; k < totalMessages; k++){    //DEBUGGING!!!!
		// 		System.out.println("Message no. " + k + " is: " + receivedMessages[k]);
		// }


		// TO-DO: If this is the last expected message, then identify
		//        any missing messages

		if(msg.messageNum == msg.totalMessages - 1) {
					// for(int i = 0; i < totalMessages; ++i)
					//  System.out.println("Receieved Message: " + Integer.toString(receivedMessages[i] + 1) + " out of " + Integer.toString(msg.totalMessages));
					System.out.println("************************************");
					System.out.println("Messages received: " + Integer.toString(totalMessages));
					System.out.println("Total messages sent: " + Integer.toString(msg.totalMessages));
					System.out.println("Success rate is: " + Double.toString((double)totalMessages / (double)msg.totalMessages * 100.0) + "%");

						// now identify any missing messages:
					if(totalMessages != msg.totalMessages) {
						System.out.println("************************************");
						System.out.println("Messages lost: ");
						for(int i = 0; i < msg.totalMessages; ++i)
							if(receivedMessages[i] != 1)
								System.out.println("Message " + Integer.toString(i) + " out of " + Integer.toString(msg.totalMessages));
					}
					totalMessages = -1;
				}

	}


		public static void main(String[] args)
		{
			RMIServer rmis = null;
			// TO-DO: Initialise Security Manager

			if (System.getSecurityManager() == null){
	        System.setSecurityManager(new SecurityManager());
	    }
			try {
						// initialise rmiserver object
						rmis = new RMIServer();

						// binding server to correct ip
						rebindServer("//146.169.53.31/RMIServer", rmis);

						System.out.println("RMIServer ready for bounding");
					} catch(Exception e) {
						System.err.println("RMIServer exception:");
						e.printStackTrace();
					}
		}

	protected static void rebindServer(String serverURL, RMIServer server)
	{
		try {
			// create registry at specific registry port
			LocateRegistry.createRegistry(1099);

			// rebinding server to the server url
			Naming.rebind(serverURL, server);

			System.out.println("RMIServer has been bound");
		} catch(Exception e) {
			System.err.println("RMIRebindServer exception:");
			e.printStackTrace();
		}

	}

}
