package com.abg.rag;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	Button toggleGarage, updateStatus;
	TCPStatus mUIUpdater;
	int status;
	TextView garageStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		garageStatus = (TextView) findViewById(R.id.tvStatus);

		// Toggle Garage Button
		toggleGarage = (Button) findViewById(R.id.bCommand);
		toggleGarage.setHapticFeedbackEnabled(true);
		toggleGarage.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Thread cThread = new Thread(new TCPClient());
				cThread.start();
			}
		});

		Thread sThread = new Thread() {
			@Override
			public void run() {

				while (true) {
					try {
						InetAddress serverAddr = InetAddress
								.getByName("abg.jumpingcrab.com");

						// Log.d("TCP", "C: Connecting...");

						Socket socket = new Socket(serverAddr, 55555);
						String message = "STATUS\0";

						socket.setSoTimeout(2000);

						// Log.d("TCP", "C: Sending: '" + message + "'");
						PrintWriter out = new PrintWriter(
								new BufferedWriter(new OutputStreamWriter(
										socket.getOutputStream())), true);
						BufferedReader in = new BufferedReader(
								new InputStreamReader(socket.getInputStream()));
						out.println(message);
						// Log.d("TCP", "C: Sent.");

						Thread.sleep(250);

						status = in.read();

						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if (status == 48) {
									garageStatus.setText("GARAGE OPEN");
									toggleGarage.setText("Close Garage");
								} else if (status == 49) {
									garageStatus.setText("GARAGE CLOSED");
									toggleGarage.setText("Open Garage");
								} else {
									garageStatus.setText("READ ERROR!");
								}
							}
						});

						// Log.d("TCP", "C: Read.");

						socket.close();
						in.close();
						out.close();

					} catch (Exception e) {
						e.printStackTrace();
					}

					// System.out.println("Thread is doing something");

					try {
						Thread.sleep(20000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		};

		sThread.start();

		updateStatus = (Button) findViewById(R.id.bGetStatus);
		updateStatus.setHapticFeedbackEnabled(true);
		updateStatus.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Thread tThread = new Thread() {
					@Override
					public void run() {
						try {
							InetAddress serverAddr = InetAddress
									.getByName("abg.jumpingcrab.com");

							// Log.d("TCP", "C: Connecting...");

							Socket socket = new Socket(serverAddr, 55555);
							String message = "STATUS\0";

							socket.setSoTimeout(2000);

							// Log.d("TCP", "C: Sending: '" + message + "'");
							PrintWriter out = new PrintWriter(
									new BufferedWriter(new OutputStreamWriter(
											socket.getOutputStream())), true);
							BufferedReader in = new BufferedReader(
									new InputStreamReader(socket
											.getInputStream()));
							out.println(message);
							// Log.d("TCP", "C: Sent.");

							Thread.sleep(250);

							status = in.read();

							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									if (status == 48) {
										garageStatus.setText("GARAGE OPEN");
										toggleGarage.setText("Close Garage");
									} else if (status == 49) {
										garageStatus.setText("GARAGE CLOSED");
										toggleGarage.setText("Open Garage");
									} else {
										garageStatus.setText("READ ERROR!");
									}
								}
							});

							// Log.d("TCP", "C: Read.");

							socket.close();
							in.close();
							out.close();

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				};
				tThread.start();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		System.exit(0);
	}

}
