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
import java.time.LocalTime;

import common.MessageInfo;

public class UDPServer {

	private DatagramSocket recvSoc;
	private int totalMessages = -1;
	private int[] receivedMessages;
	private boolean close;
	private boolean fmess = true;
	private int delay = 30000;
	private boolean first = true;
	private String lastmess;

	private void run() {
		int				pacSize;
		byte[]			pacData;
		DatagramPacket 	pac;

		// TO-DO: Receive the messages and process them by calling processMessage(...).
		//        Use a timeout (e.g. 30 secs) to ensure the program doesn't block forever

		while(!close){
			pacSize = 5000;
			pacData = new byte[5000];
			pac = new DatagramPacket(pacData, pacSize);
			try{
				recvSoc.setSoTimeout(delay);
				recvSoc.receive(pac);
				first = false;
			}
			catch (IOException e){
				System.out.println("Timeout!");
				if(!first){
					processMessage("-1");
				}
				System.exit(-1);
			}

			String data = new String(pac.getData(), pac.getOffset(), pac.getLength());
			processMessage(data);
		}

	}

	public void processMessage(String data) {

		String lost = "Messages Lost: ";

		if(data == "-1"){
			String[] fields = lastmess.split(";");
			int tot = Integer.parseInt(fields[0]);
			double no = 0;
			for (int i=1; i<=tot; i++){
				if(receivedMessages[i] == 1){
					no++;
				}
				else{
					lost = lost+" "+i+"\n";
				}
			}
			double total = (no/tot)*100;
			System.out.println(lost);
			System.out.println("Number of Messages Recived: "+no);
			System.out.println("Percentage of Messages Recieved: "+total+"%");
			close = true;
			System.exit(-1);
		}

		lastmess = data;


		MessageInfo msg = null;

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
			System.out.println("Exception somehow");
		}

		// TO-DO: On receipt of first message, initialise the receive buffer

		if(fmess){
			receivedMessages = new int[msg.totalMessages+1];
			fmess = false;
		}

		// TO-DO: Log receipt of the message
		receivedMessages[msg.messageNum] = 1;
		// TO-DO: If this is the last expected message, then identify
		//        any missing messages

		if(msg.totalMessages == msg.messageNum){
			double no = 0;
			for (int i=1; i<=msg.totalMessages; i++){
				if(receivedMessages[i] == 1){
					no++;
				}
				else{
					lost = lost+" "+i+"\n";
				}
			}
			double total = (no/msg.totalMessages)*100;
			System.out.println(lost);
			System.out.println("Number of Messages Recived: "+no);
			System.out.println("Percentage of Messages Recieved: "+total+"%");
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
