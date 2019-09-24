package com.ensuque;

import com.ensuque.collab.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static ServerSocket serverSocket;

    private static int port = 1700;

    public static void main(String[] args) {


        //Start the server
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException err) {
            System.out.println("Failed to start server!");
            System.out.println(err.toString());
        }

        System.out.println("Server is launched on " + serverSocket.getLocalSocketAddress().toString());
        System.out.println("Waiting for clients to perform CollabRequests...");

        //Wait for CollabRequests to perform.
        while (true) {

            try {

                //Make the user choose a request, send it to server, receive a result and display it.
                receiveAndRunCollabRequest();

            } catch (IOException ioe) {
                System.out.println("Connection error with the client!");
                System.out.println(ioe.toString());
                ioe.printStackTrace();
            }
        }
    }

    /**
     * Wait that a Client connects to the server and send a CollabRequest, then perform it and send back the result
     * to the client as a CollabResponse before ending the connection.
     * @throws IOException
     */
    private static void receiveAndRunCollabRequest() throws IOException {

        Socket socket;
        ObjectOutputStream oos;
        ObjectInputStream ois;

        //Wait for client to connect
        socket = serverSocket.accept();

        System.out.println("--------------");

        //Setup streams
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());

        CollabResponse response;

        try {

            //Receive the CollabRequest from the client
            CollabRequest<?> collabRequest = (CollabRequest)ois.readObject();

            System.out.println("CollabRequest received from " + socket.getInetAddress().toString() + ":" + socket.getPort());
            System.out.println(collabRequest.toString());

            //Execute it and obtain a CollabResponse with the result of the calculation.
            response = collabRequest.run();

            System.out.println("CollabRequest performed.");

        } catch (ClassNotFoundException err) {
            //If there was an error, returns an empty CollabResponse containing the error.
            System.out.println(err.toString());
            response = new CollabResponse(null, false, err);
        } catch (ClassCastException cce) {
            //If there's an error, returns an empty CollabResponse containing the error.
            System.out.println("The received object is not of type CollabRequest!");
            response = new CollabResponse(null, false, cce);
        }

        //Send response to client.
        oos.writeObject(response);

        System.out.println("CollabResponse sent, end of connection.");

        socket.close();
    }


}
