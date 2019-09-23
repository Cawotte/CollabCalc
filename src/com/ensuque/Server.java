package com.ensuque;

import com.ensuque.collab.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {


    private static ServerSocket serverSocket;

    public static void main(String[] args) throws Exception {

        int port = 1700;

        //Start the server
        serverSocket = new ServerSocket(port);

        System.out.println("Server is launched on " + serverSocket.getLocalSocketAddress().toString());
        System.out.println("Waiting for clients to perform CollabRequests...");

        //Wait for CollabRequests to perform.
        while (true) {
            receiveAndRunCollabRequest();
        }
    }

    /**
     * Wait that a Client connects to the server and send a CollabRequest, then perform it and send back the result
     * to the client as a CollabResult before ending the connection.
     * @throws Exception
     */
    public static void receiveAndRunCollabRequest() throws Exception {

        Socket socket;
        ObjectOutputStream oos;
        ObjectInputStream ois;

        socket = serverSocket.accept(); //wait for client

        System.out.println("--------------");

        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());

        CollabRequest<?> collabRequest = (CollabRequest)ois.readObject();
        System.out.println("CollabRequest received from " + socket.getInetAddress().toString() + " / " + socket.getPort());
        System.out.println(collabRequest.toString());

        CollabResult result = collabRequest.run();

        System.out.println("CollabRequest performed.");

        oos.writeObject(result);

        System.out.println("CollabResult sent, end of connection.");

        socket.close();
    }


}
