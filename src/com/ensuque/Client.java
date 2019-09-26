package com.ensuque;

import com.ensuque.collab.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static boolean toQuit = false;

    protected static int port = 1700;
    protected static String ipServer = "127.0.0.1";

    //localhost 127.0.0.1
    //Fabien 192.168.0.128
    //Elie 192.168.0.185

    public static void main(String[] args) {


        CollabRequest<?> collabRequest;
        CollabResponse response;

        //Ask the user to choose a CollabRequest to perform
        collabRequest = chooseCollabRequest();

        //While the client hasn't asked to quit
        while (!toQuit) {

            if (collabRequest != null) {

                try {
                    //Send a collabRequest and wait its response
                    response = sendAndReceiveCollab(collabRequest, ipServer, port, true);
                    showResult(response);
                } catch (IOException err) {
                    System.out.println("Connection error with server! Connection aborted.");
                    System.out.println(err.toString());
                }
            }

            collabRequest = chooseCollabRequest();
        }

        System.out.println("Quitting...");
    }

    //region Protected Methods

    /**
     * Print the CollabResponse info : The result if it's successful, or the exception if there was one.
     * @param response
     */
    protected static void showResult(CollabResponse response) {
        if (response == null) {
            System.out.println("CollabResponse is null!");
        }
        else if (response.isSuccessful()) {
            System.out.println("Result received : " + response.getResult().toString());
        }
        else {
            System.out.println("No result, an error has occurred : ");
            response.printError();
            response.getError().printStackTrace();
        }
    }

    /**
     * Send a CollabRequest to a distant server, wait for a CollabResponse reply, and returns it.
     * @param collab
     * @param ipServer
     * @param port
     * @param verbose
     * @return CollabResponse sent by server.
     * @throws IOException
     */
    protected static CollabResponse sendAndReceiveCollab(CollabRequest collab, String ipServer, int port,
                                                         boolean verbose) throws IOException
    {

        Socket socket;
        ObjectOutputStream oos;
        ObjectInputStream ois;
        CollabResponse response;

        if (verbose)
            System.out.println("---- Sending New CollabRequest ----");

        //Connect to server
        socket = new Socket(ipServer, port);

        //Setup streams
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());

        //Send CollabRequest
        oos.writeObject(collab);

        if (verbose)
            System.out.println("CollabRequest sent.");

        try {

            //Wait for CollabResponse reply
            response = (CollabResponse)ois.readObject();

        } catch (ClassNotFoundException err) {
            //Error when receiving, returns null.
            System.out.println("Error while receiving the response :");
            System.out.println(err.toString());

            socket.close();
            return null;
        }


        if (verbose)
            System.out.println("CollabResponse received ! End of connection.");

        //End connection
        socket.close();

        return response;
    }

    /**
     * Instantiate a CollabRequest with the given parameters and returns it. If an exception is raised, handles and prints it.
     * @param obj
     * @param methodName
     * @param args
     * @return
     */
    protected static CollabRequestObject tryCreatingCollabRequest(Object obj, String methodName, Object[] args) {

        try {
            return new CollabRequestObject<>((Serializable)obj, methodName, args);
        }
        catch (Exception err) {
            System.out.println("Exception when instantiating CollabRequest :\n\t" + err.toString());
            return null;
        }
    }

    //endregion

    /**
     * Print a CollabRequest choice and makes the user input its method choice and the value of its parameters.
     * @return
     */
    private static CollabRequest chooseCollabRequest() {

        String methodName;
        Object[] params;
        Calc calc = new Calc();

        System.out.println("Choose a CollabRequest to perform by typing the appropriate number :" +
                "\n 1 - Addition" +
                "\n 2 - Multiply" +
                "\n 3 - Divide" +
                "\n 0 - Quit");

        Scanner sc = new Scanner(System.in);
        int choice = -1;

        //Select a choice
        while (choice < 0 || choice > 3) {

            choice = inputAnyInteger(sc);
            if (choice < 0 || choice > 3) {
                System.out.println("Please enter a valid number.");
            }
        }

        //Choose the method's parameters (user inputs)
        switch(choice) {
            case 1:
                methodName = "add";

                Integer a, b;
                System.out.println("Addition : a + b");
                System.out.println("Please enter a value for a :");
                a = inputAnyInteger(sc);
                System.out.println("Please enter a value for b :");
                b = inputAnyInteger(sc);

                //Select a
                params = new Object[]{a.toString(), b.toString()};
                break;
            case 2:
                methodName = "multiply";

                Float x, y;
                System.out.println("Multiply : x * y");
                System.out.println("Please enter a decimal value for x :");
                x = inputAnyFloat(sc);
                System.out.println("Please enter a decimal value for y :");
                y = inputAnyFloat(sc);

                params = new Object[]{x, y};
                break;
            case 3:
                methodName = "divide";

                Float x1, y1;
                System.out.println("Divide : x / y");
                System.out.println("Please enter a decimal value for x :");
                x1 = inputAnyFloat(sc);
                System.out.println("Please enter a decimal value for y :");
                y1 = inputAnyFloat(sc);

                params = new Object[]{x1, y1};
                break;
            default:
                toQuit = true;
                return null; //end choice

        }

        // -- MAKE REQUEST
        return tryCreatingCollabRequest(calc,
                methodName,
                params);
    }

    /**
     * Wait for the user to input an integer an returns it. Loop until they input an integer.
     * @param sc
     * @return
     */
    private static Integer inputAnyInteger(Scanner sc) {
        Integer number = null;
        while (number == null) {
            if (sc.hasNextInt()) {
                number = sc.nextInt();
            }
            else {
                sc.next();
                System.out.println("Please enter a valid number.");
            }
        }
        return number;
    }

    /**
     * Wait for the user to input a float an returns it. Loop until they input a float.
     * @param sc
     * @return
     */
    private static Float inputAnyFloat(Scanner sc) {
        Float number = null;
        while (number == null) {
            if (sc.hasNextFloat()) {
                number = sc.nextFloat();
            }
            else {
                sc.next();
                System.out.println("Please enter a valid decimal number.");
            }
        }
        return number;
    }

}
