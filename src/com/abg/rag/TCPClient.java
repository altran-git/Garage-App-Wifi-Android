package com.abg.rag;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class TCPClient implements Runnable {

	public void run() {

		try {

			InetAddress serverAddr = InetAddress.getByName("abg.jumpingcrab.com");

			//Log.d("TCP", "C: Connecting...");

			Socket socket = new Socket(serverAddr, 55555);
			
			socket.setSoTimeout(2000);

			String message = "#$13vdY%1$@&8Ggk@3!\0";

			//Log.d("TCP", "C: Sending: '" + message + "'");
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			out.println(message);
				
			//Thread.sleep(20000);
				
			//message = "STATUS\0";
			//out.println(message);
				
			//Log.d("TCP", "C: Sent.");

			socket.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			//Log.e("TCP", "C: Error", e);
		}
	}
}