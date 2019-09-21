package com.ensuque;

import com.ensuque.collab.CollabRequest;
import com.ensuque.collab.CollabResult;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws Exception {


        int port = 1700;
        String ipServer = "127.0.0.1";

        //localhost 127.0.0.1
        //Fabien 192.168.0.128
        //Elie 192.168.0.185

        //System.out.println(InetAddress.getLocalHost().getHostAddress());
        //System.out.println(InetAddress.getLocalHost().getHostName());

        CollabRequest<?> collabRequest;
        CollabResult result;

        collabRequest = chooseCollabRequest();

        while (collabRequest != null) {
            result = sendAndReceiveCollabRequest(collabRequest, ipServer, port);
            System.out.println("Result = " + result.getResult().toString());
            collabRequest = chooseCollabRequest();
        }
    }

    private static CollabResult sendAndReceiveCollabRequest(CollabRequest collab, String ipServer, int port)
                                                    throws Exception
    {

        Socket socket;
        ObjectOutputStream oos;
        ObjectInputStream ois;
        CollabResult result;

        System.out.println("------ COLLAB REQUEST --------");
        System.out.println("Connecting to " + ipServer + "...");
        socket = new Socket(ipServer, port);

        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());

        System.out.println("Send Request...");

        oos.writeObject(collab);
        System.out.println("Receive result...");

        result = (CollabResult)ois.readObject();


        System.out.println("Received ! End of connection.");
        System.out.println("--------------");

        socket.close();

        return result;
    }

    private static CollabRequest chooseCollabRequest() {

        String methodName;
        Object[] params;
        Calc calc = new Calc();

        System.out.println("Choose a collab request to perform by typing the number :" +
                "\n 1 - Addition 5 + 2" +
                "\n 2 - Multiply 5 * 2" +
                "\n Others - Quit");
        Scanner sc = new Scanner(System.in);

        int choice = sc.nextInt();
        switch(choice) {
            case 1:
                methodName = "add";
                params = new Object[]{"5", "2"};
                break;
            case 2:
                methodName = "multiply";
                params = new Object[]{5f, 2f};
                break;
            default:
                return null; //end choice

        }

        // -- MAKE REQUEST
        CollabRequest<Calc> collab;
        try {

            collab = new CollabRequest<>(
                    calc,
                    methodName,
                    params);
        } catch (Exception err) {
            System.out.println(err.getStackTrace());
            return null;
        }
        return collab;
    }
}
