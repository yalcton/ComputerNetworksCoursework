/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.rmi.RMISecurityManager;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import common.MessageInfo;

public class RMIClient {

	public static void main(String[] args) {

		RMIServerI rmis = null;

		// Check arguments for Server host and number of messages
		if (args.length < 2){
			System.out.println("Needs 2 arguments: ServerHostName/IPAddress, TotalMessageCount");
			System.exit(-1);
		}

		String urlServer = new String("rmi://" + args[0] + "/RMIServer");
		int numMessages = Integer.parseInt(args[1]);

		// TO-DO: Initialise Security Manager

		if (System.getSecurityManager() == null) {
        System.setSecurityManager(new SecurityManager());
    }

		// TO-DO: Bind to RMIServer
		try {
			rmis = (RMIServerI)Naming.lookup(urlServer);
		} catch(Exception e) {
			System.err.println("RMIClient binding exception:");
			e.printStackTrace();
		}

		// TO-DO: Attempt to send messages the specified number of times

		for(int i = 0; i < numMessages; i++)
		{
				MessageInfo msg = new MessageInfo(numMessages, i);
				try {
					rmis.receiveMessage(msg);
				} catch(Exception e) {
					System.err.println("RMIClient message exception:");
					e.printStackTrace();
				}
			}
}
}
