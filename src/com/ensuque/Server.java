package com.ensuque;

import com.ensuque.model.CollabRequest;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {


    private static ServerSocket serverSocket;

    public static void main(String[] args) throws Exception {

        int port = 1700;

        serverSocket = new ServerSocket(port);

        while (true) {
            receiveAndRunCollabRequest();
        }
    }

    public static void receiveAndRunCollabRequest() throws Exception {

        Socket socket;
        ObjectOutputStream oos;
        ObjectInputStream ois;

        System.out.println("Waiting for a client...");
        socket = serverSocket.accept(); //wait for client
        System.out.println("Connection Established.");

        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());

        System.out.println("Waiting for CollabRequest...");
        CollabRequest<?> collabRequest = (CollabRequest)ois.readObject();
        System.out.println("CollabRequest Received...");

        Object result = collabRequest.run();

        System.out.println("Result = " + result.toString());


        System.out.println("Send Result...");
        oos.writeObject(result);


        System.out.println("End of connection...");
        System.out.println("--------------");

        socket.close();
    }


}
