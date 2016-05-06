package com.abg.rag;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import android.util.Log;


public class TCPStatus implements Runnable
{
	int status;
	
    public void run()
    {
    	
        while(true) {
        	try {
    			InetAddress serverAddr = InetAddress
    					.getByName("abg.jumpingcrab.com");

    			Log.d("TCP", "C: Connecting...");

    			Socket socket = new Socket(serverAddr, 55555);
    			String message = "STATUS\0";


    			Log.d("TCP", "C: Sending: '" + message + "'");
    			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
    			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    			out.println(message);
    			Log.d("TCP", "C: Sent.");

    			status = in.read();
    			
    			
    			Log.d("TCP", "C: Read.");
    			
    			socket.close();
    			in.close();
    			out.close();
    			
    		} catch(Exception e){
    			e.printStackTrace();
    		}
        	
        	System.out.println("Thread is doing something");
        	
            
        	try {
        		Thread.sleep(5000);
        	} catch (InterruptedException e) {
        		e.printStackTrace();
        	}
        }
    }
}