package com.ensuque;

import com.ensuque.model.CollabRequest;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {



    public static void main(String[] args) throws Exception {


        int port = 1700;
        ServerSocket serverSocket;
        Socket socket;
        ObjectOutputStream oos;
        ObjectInputStream ois;

        serverSocket = new ServerSocket(port);
        System.out.println("Waiting for a client..s.");
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


        System.out.println("End connection...");

        socket.close();
    }

    public static CollabRequest receiveCollabRequest() {

        return null;
    }

}
