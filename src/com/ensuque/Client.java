package com.ensuque;

import com.ensuque.collab.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws Exception {


        int port = 1700;
        String ipServer = "127.0.0.1";

        //localhost 127.0.0.1
        //Fabien 192.168.0.128
        //Elie 192.168.0.185


        CollabRequest<?> collabRequest;
        CollabResult result;

        collabRequest = chooseCollabRequest();

        while (collabRequest != null) {
            result = sendAndReceiveCollabRequest(collabRequest, ipServer, port);
            System.out.println("Result = " + result.getResult().toString());
            collabRequest = chooseCollabRequest();
        }
    }

    protected static CollabResult sendAndReceiveCollabRequest(CollabRequest collab, String ipServer, int port)
                                                    throws Exception
    {

        Socket socket;
        ObjectOutputStream oos;
        ObjectInputStream ois;
        CollabResult result;

        System.out.println("---- COLLAB REQUEST ----");
        socket = new Socket(ipServer, port);

        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());

        oos.writeObject(collab);
        System.out.println("CollabRequest sent.");

        result = (CollabResult)ois.readObject();


        System.out.println("CollabRequest received ! End of connection.");

        socket.close();

        return result;
    }

    protected static CollabRequest tryCreatingCollabRequest(Object obj, String methodName, Object[] args) {

        try {
            return new CollabRequest<>((Serializable)obj, methodName, args);
        }
        catch (Exception err) {
            System.out.println("Exception when instantiating CollabRequest :\n\t" + err.toString());
            return null;
        }
    }
    protected static String methodToString(Method method) {
        Class<?>[] paramTypes = method.getParameterTypes();
        String str = method.getName() + "(";
        for (int i = 0; i < paramTypes.length; i++) {
            str += paramTypes[i].getSimpleName();
            if (i != paramTypes.length - 1)
                str += ", ";
        }
        str += ")";
        return str;
    }

    private static CollabRequest chooseCollabRequest() {

        String methodName;
        Object[] params;
        Calc calc = new Calc();
        chooseCollabRequestFromClass(calc);

        System.out.println("Choose a collab request to perform by typing the appropriate number :" +
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

    private static void chooseCollabRequestFromClass(Object obj) {
        //Choose a request

        Class<?> clazz = obj.getClass();
        Method[] allMethods = clazz.getDeclaredMethods();
        ArrayList<Method> publicMethods = new ArrayList<>();

        //get all public methods
        for (Method method : allMethods) {
            //if the method is public
            if (Modifier.isPublic(method.getModifiers())) {
                publicMethods.add(method);
            }
        }

        System.out.println("Select a method by typing the associated number : ");
        for (int i = 0; i < publicMethods.size(); i++) {
            System.out.println((i + 1) + " - " + methodToString(publicMethods.get(i)));
        }

        //Pick a method from the list
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        while (choice == 0) {
            if (sc.hasNextInt()) {
                choice = sc.nextInt();
            }
            else {
                sc.next();
                System.out.println("Please enter a valid number.");
            }
        }

        Method method = publicMethods.get(choice - 1);
        System.out.println("You chose " + methodToString(method));

        //Pick its arguments values


    }

}
