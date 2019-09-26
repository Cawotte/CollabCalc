package com.ensuque;

import com.ensuque.collab.*;

import java.io.IOException;
import java.util.ArrayList;

/**
 * An automated Client to test several use cases of CollabRequest.
 */
public class AutoClient extends Client {

    protected static int port = 1700;
    protected static String ipServer = "127.0.0.1";

    public static void main(String[] args) {

        try {
            Calc calc = new Calc();
            CollabRequest request = new CollabRequestClass(calc.getClass(), "add", new Object[]{"5", "10"});

            CollabResponse response = request.run();
            showResult(response);

        } catch (Exception err) {
            System.out.println(err.toString());
            err.printStackTrace();
        }

        ArrayList<CollabRequest> collabRequests = new ArrayList<>();

        //Fill the array with examples to test the CollabRequests
        setupExamples(collabRequests);

        //Perform all requests
        for (int i = 0; i < collabRequests.size(); i++) {

            CollabRequest request = collabRequests.get(i);

            System.out.println("\nTest #" + (i+1));
            System.out.println("CollabRequest : " + request.toString());

            try {
                //Send the request and wait for the result.
                CollabResponse result = sendAndReceiveCollab(request, ipServer, port, false);

                showResult(result);

            } catch (IOException err) { //Connection errors
                System.out.println("Connection error ! Request aborted...");
                System.out.println(err.toString());
            }
        }


    }

    private static void setupClassExamples(ArrayList<CollabRequest> requests) {

    }
    /**
     * Fill the given CollabRequest list with several predefinied use cases.
     * @param requests
     */
    private static void setupExamples(ArrayList<CollabRequest> requests) {

        Calc calc = new Calc();

        //Test 1 - Working addition
        tryAddingCollabRequest(
                requests,
                calc,
                "add",
                new Object[]{"5", "10"}
        );

        //Test 2 - Working multiplication
        tryAddingCollabRequest(
                requests,
                calc,
                "multiply",
                new Object[]{5f, 10f}
        );

        //Test 3 - Addition with wrong parameters types
        //Throws an error at CollabRequest instantiation
        tryAddingCollabRequest(
                requests,
                calc,
                "add",
                new Object[]{5, 0}
        );
        System.out.println("The method 'add' uses int as parameters, however, CollabRequest " +
                "requires NO primitive type in the arguments, throwing an error.\n");

        //Test 4 - Divide by Zero
        tryAddingCollabRequest(
                requests,
                calc,
                "divide",
                new Object[]{5f, 0f}
        );

        //Test 5 - No Such Method
        tryAddingCollabRequest(
                requests,
                calc,
                "addition",
                new Object[]{"5", "10"}
        );
        System.out.println("There's no method named 'addition' in the class Calc.\n");

    }

    /**
     * Generate a CollabRequest with the given parameters, and if it's valid (no thrown exception), add it to the given list.
     * @param requests List of request to add the new CollabRequest.
     * @param obj
     * @param methodName
     * @param args
     */
    private static void tryAddingCollabRequest(ArrayList<CollabRequest> requests, Object obj, String methodName, Object[] args) {
        CollabRequest request = tryCreatingCollabRequest(obj, methodName, args);
        if (request != null) {
            requests.add(request);
        }
    }
}
