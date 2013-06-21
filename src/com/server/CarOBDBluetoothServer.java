package com.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import com.util.ConnectionManager;
import com.util.OBDModel;

/**
 * Class that implements an SPP Server which accepts single line of message from
 * an SPP client and sends a single line of response to the client.
 */
public class CarOBDBluetoothServer {

	// start server
	private void startServer() throws IOException {

		// Create a UUID for SPP
		UUID uuid = new UUID("1101", true);

		// Create the Servicve URL
		String connectionString = "btspp://localhost:" + uuid
				+ ";name=Sample SPP Server";

		// open server url
		StreamConnectionNotifier streamConnNotifier = (StreamConnectionNotifier)Connector.open(connectionString);

		// Wait for client connection
		System.out.println("\nServer Started. Waiting for Clients to connect...");
		while (true) {
			try {
				StreamConnection connection = streamConnNotifier
						.acceptAndOpen();
				RemoteDevice dev = RemoteDevice.getRemoteDevice(connection);
				System.out.println("Remote device address: "
						+ dev.getBluetoothAddress());
				System.out.println("Remote device name: "+dev.getFriendlyName(true));

				// read string from spp client
				// InputStream inStream=connection.openInputStream();
				// BufferedReader bReader=new BufferedReader(new
				// InputStreamReader(inStream));
				// String lineRead=bReader.readLine();
				// System.out.println(lineRead);
				// send response to spp client
System.out.println("Got Incoming Request "+new Date());
				OutputStream outStream = connection.openOutputStream();
				OBDModel o = ConnectionManager.getOBDList();
				ObjectOutputStream os = new ObjectOutputStream(outStream);
				os.writeObject(o);
				os.flush();
				os.close();
				// outStream.close();
				// connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// pWriter.close();
		// if(streamConnNotifier!=null){
		// streamConnNotifier.close();
		// }
	}

	public static void main(String[] args) throws IOException {
		// display local device address and name
		LocalDevice localDevice = LocalDevice.getLocalDevice();
		System.out.println("Address: " + localDevice.getBluetoothAddress());
		System.out.println("Name: " + localDevice.getFriendlyName());
		CarOBDBluetoothServer sampleSPPServer = new CarOBDBluetoothServer();
		sampleSPPServer.startServer();
	}

}