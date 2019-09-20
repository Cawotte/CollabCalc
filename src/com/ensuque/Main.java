package com.ensuque;

import com.ensuque.model.Calc;
import com.ensuque.model.CollabRequest;

import java.lang.reflect.Method;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
	// write your code here

        String methodName;
        Object[] params;
        Calc calc = new Calc();

        // -- TEST LOCAL
        System.out.println("5 + 2 = " + calc.add("5", "2"));
        System.out.println("5 * 2 = " + calc.multiply(5f, 2f));

        // -- START CHOICE
        System.out.println("Now with request...");

        System.out.println("0 for Addition, else will multiply...");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        if (choice == 0) {
            methodName = "add";
            params = new Object[]{"5", "2"};
        }
        else {
            methodName = "multiply";
            params = new Object[]{5f, 2f};
        }

        // -- MAKE REQUEST
        CollabRequest<Calc> collab;
        try {

            collab = new CollabRequest<>(
                    calc,
                    methodName,
                    params);
        } catch (NoSuchMethodException err) {
            System.out.println(err.getStackTrace());
            return;
        }


        System.out.println("Result = " + collab.run());

        System.out.println("Done!");

    }

    private static void printMethods(Object obj) {
        Method[] methods = obj.getClass().getMethods();

        System.out.println("Méthodes de " + obj.getClass().getName());
        for (int i = 0; i < methods.length; i++) {
            System.out.println(methods[i].getName());
        }
    }
}
