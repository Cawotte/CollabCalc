package com.ensuque;

import com.ensuque.collab.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static ServerSocket serverSocket;

    public static void main(String[] args) {

        int port = 1700;

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
                receiveAndRunCollabRequest();
            } catch (IOException ioe) {
                System.out.println("Erreur de connection avec un client!");
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

        CollabResponse result;

        try {

            //Receive the CollabRequest from the client
            CollabRequest<?> collabRequest = (CollabRequest)ois.readObject();

            System.out.println("CollabRequest received from " + socket.getInetAddress().toString() + ":" + socket.getPort());
            System.out.println(collabRequest.toString());

            //Execute it and obtain a CollabResponse with the result of the calculation.
            result = collabRequest.run();

            System.out.println("CollabRequest performed.");

        } catch (ClassNotFoundException err) {
            //If there's an error, returns an empty CollabResponse containing the error.
            System.out.println(err.toString());
            result = new CollabResponse(null, false, err);
        } catch (ClassCastException cce) {
            //If there's an error, returns an empty CollabResponse containing the error.
            System.out.println("The received object is not of type CollabRequest!");
            result = new CollabResponse(null, false, cce);
        }

        oos.writeObject(result);

        System.out.println("CollabResponse sent, end of connection.");

        socket.close();
    }


}
