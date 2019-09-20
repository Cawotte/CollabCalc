package com.ensuque;

import com.ensuque.model.Calc;
import com.ensuque.model.CollabRequest;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws Exception {


        int port = 1700;
        String ipServer = "127.0.0.1";


        CollabRequest<?> collabRequest;
        Object result;

        collabRequest = chooseCollabRequest();

        while (collabRequest != null) {
            result = sendAndReceiveCollabRequest(collabRequest, ipServer, port);
            System.out.println("Result = " + result.toString());
            collabRequest = chooseCollabRequest();
        }
    }

    private static Object sendAndReceiveCollabRequest(CollabRequest collab, String ipServer, int port)
                                                    throws Exception
    {

        Socket socket;
        ObjectOutputStream oos;
        ObjectInputStream ois;
        Object result;

        System.out.println("Connecting to " + ipServer + "...");
        socket = new Socket(ipServer, port);
        System.out.println("Connection Established.");

        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());

        System.out.println("Send Request...");

        oos.writeObject(collab);
        System.out.println(" Wait for answer...");

        result = ois.readObject();


        System.out.println("End connection...");

        socket.close();

        return result;
    }

    private static CollabRequest chooseCollabRequest() {

        String methodName;
        Object[] params;
        Calc calc = new Calc();

        System.out.println("0 for Addition, else will multiply...");
        Scanner sc = new Scanner(System.in);

        int choice = sc.nextInt();
        if (choice == 1) {
            methodName = "add";
            params = new Object[]{"5", "2"};
        }
        else if (choice == 2) {
            methodName = "multiply";
            params = new Object[]{5f, 2f};
        }
        else {
            return null;
        }

        // -- MAKE REQUEST
        CollabRequest<Calc> collab;
        try {

            collab = new CollabRequest<Calc>(
                    calc,
                    methodName,
                    params);
        } catch (NoSuchMethodException err) {
            System.out.println(err.getStackTrace());
            return null;
        }
        return collab;
    }
}
