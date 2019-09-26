package com.ensuque.network;

import com.ensuque.testComputing.*;
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


        //Object oriented method
        System.out.println("\n------ Tests with CollabRequestObject (OBJECT METHOD) ----- ");

        ArrayList<CollabRequest> collabRequests = new ArrayList<>();
        strategyCollabRequest = new StrategyCollabRequestObject();

        addExamplesCollabRequests(collabRequests);
        sendAndReceiveAllRequests(collabRequests);


        //Bytecode oriented method
        System.out.println("\n\n------ Tests with CollabRequestBytecode (BYTECODE METHOD) ----- ");

        collabRequests = new ArrayList<>();
        strategyCollabRequest = new StrategyCollabRequestBytecode();

        addExamplesCollabRequests(collabRequests);
        sendAndReceiveAllRequests(collabRequests);
    }

    /**
     * Send all the CollabRequests to the server, wait for a result and display it.
     * @param collabRequests
     */
    private static void sendAndReceiveAllRequests(ArrayList<CollabRequest> collabRequests) {

        System.out.println("--- SENDING ALL STORED COLLAB REQUEST ---");

        //Perform all requests
        for (int i = 0; i < collabRequests.size(); i++) {

            CollabRequest request = collabRequests.get(i);

            System.out.println("\tTest #" + (i+1));
            System.out.println(request.toString());

            try {
                //Send the request and wait for the result.
                CollabResponse result = sendAndReceiveCollab(request, ipServer, port, false);

                showResult(result);

            } catch (IOException err) { //Connection errors
                System.out.println("\tConnection error ! Request aborted. " + err.toString());
            }
        }
    }



    /**
     * Fill the given CollabRequest list with several predefined use cases.
     * The CollabStrategy needs to be set beforehand.
     * @param requests
     */
    private static void addExamplesCollabRequests(ArrayList<CollabRequest> requests) {

        calc = new CalcSerializable();

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
        System.out.println("\tExplanation : The method 'add' uses int as parameters, however, CollabRequest " +
                "requires NO primitive type in the arguments, throwing an error.");

        //Test 4 - Divide by Zero
        tryAddingCollabRequest(
                requests,
                calc,
                "divide",
                new Object[]{5f, 0f}
        );
        System.out.println("\tExplanation : This is a valid request, but it will return a failed execution because of" +
                " a divide by zero.");

        //Test 5 - No Such Method
        tryAddingCollabRequest(
                requests,
                calc,
                "addition",
                new Object[]{"5", "10"}
        );
        System.out.println("\tExplanation : There's no method named 'addition' in the class Calc.");

        calc = new Calc(); //The not serializable calc!

        //Test 5 - Unserializable Calc
        tryAddingCollabRequest(
                requests,
                calc,
                "add",
                new Object[]{"5", "10"}
        );
        System.out.println("\tExplanation : Calc is not a Serializable class. That CollabRequest cannot works with" +
                "the Object strategy because it requires serializable object, however it will works with the" +
                "Bytecode methods because it doesn't require the object to be sent over the network.\n");

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
            System.out.println("CollabRequest generated : " + request.toString());
        }
    }
}
