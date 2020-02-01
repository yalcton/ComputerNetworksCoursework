/*
 * Created on 01-Mar-2016
 */
package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;

import common.MessageInfo;

public class UDPServer {

	private DatagramSocket recvSoc;
	private int totalMessages = -1;
	private int[] receivedMessages;
	private boolean close;

	private void run() {
		int				pacSize;
		byte[]			pacData;
		DatagramPacket 	pac;

		// TO-DO: Receive the messages and process them by calling processMessage(...).
		//        Use a timeout (e.g. 30 secs) to ensure the program doesn't block forever

		int timeout = 30000;

		while(!close){
			pacSize = 5000;
			pacData = new byte[5000];

			pac = new DatagramPacket(pacData, pacSize);
			try{
				recvSoc.setSoTimeout(timeout);
				recvSoc.receive(pac);
			}
			catch (IOException e){
				System.out.println("Error in running program");
				System.exit(-1);
			}

			String data = new String(pac.getData(), pac.getOffset(), pac.getLength());
			processMessage(data);
		}

	}

	public void processMessage(String data) {

		MessageInfo msg = null;
		System.out.println(data);

		// TO-DO: Use the data to construct a new MessageInfo object

		try{
			msg = new MessageInfo(data);
		}
		catch(ClassNotFoundException e) {
			System.out.println("Class not found");
		}
		catch(IOException e){
			System.out.println("IO Exception");
		}
		catch(Exception e){
			System.out.println("Exception");
		}
		// TO-DO: On receipt of first message, initialise the receive buffer

		if(receivedMessages == null){
				totalMessages = msg.totalMessages;
				receivedMessages = new int[totalMessages];
		}

		// TO-DO: Log receipt of the message
		receivedMessages[msg.messageNum] = 1;

		// TO-DO: If this is the last expected message, then identify
		//        any missing messages
		if(msg.messageNum == msg.totalMessages){
			String lost = "Not recieved messages: ";

			int no = 0;
			for (int i=0; i<totalMessages; i++){
				if(receivedMessages[i] != 1){
					no++;
					lost = lost + " " + (i+1) + ", ";
				}
			}

			if (no == 0){
				lost = lost + "NONE";
			}

			System.out.println(lost);
			close = true;
		}

	}


	public UDPServer(int rp) {
		// TO-DO: Initialise UDP socket for receiving data


		try{
			recvSoc = new DatagramSocket(rp);
		}
		catch (SocketException e) {
			System.out.println("couldn't create server on port" + rp);
			System.exit(-1);
		}

		// Done Initialisation
		System.out.println("UDPServer ready");

		close = false;
	}


	public static void main(String args[]) {
		int	recvPort;

		// Get the parameters from command line
		if (args.length < 1) {
			System.err.println("Arguments required: recv port");
			System.exit(-1);
		}

		recvPort = Integer.parseInt(args[0]);

		// TO-DO: Construct Server object and start it by calling run().
		UDPServer udps = new UDPServer(recvPort);

		udps.run();

	}

}
