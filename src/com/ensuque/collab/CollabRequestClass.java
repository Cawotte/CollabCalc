package com.ensuque.collab;

import com.ensuque.Calc;
import com.sun.security.jgss.GSSUtil;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URISyntaxException;

public class CollabRequestClass<T> extends CollabRequest implements Serializable {

    //Object with the method to execute.
    private Class<T> clazz;
    private byte[] byteArray;

    public CollabRequestClass(Class<T> clazz, String methodName, Object[] args)
            throws NoSuchMethodException, InvalidReturnType, FileNotFoundException, URISyntaxException {
        super(clazz, methodName, args);
        this.clazz = clazz;

        this.byteArray = classToByteArray();
    }

    @Override
    protected T getClassObject() {

        //Unserialize File
        JavaClassLoader _classLoader = new JavaClassLoader(byteArray);

        Class regeneratedClass = _classLoader.loadClass();

        try {
            /*
            Object obj = regeneratedClass.getConstructor().newInstance();
            Object result = getMethod(regeneratedClass).invoke(obj, args);
            System.out.println(result.toString());
            Object result = obj.getClass().getDeclaredMethod("add", new Class[]{"5".getClass(), "5".getClass()}).invoke(obj,
                    "5", "4"
            );

            System.out.println("answer : " + result.toString());*/


            return (T)regeneratedClass.getConstructor().newInstance();
        } catch (Exception err) {
            System.out.println("Error while instantiating class " + regeneratedClass.getSimpleName());
            System.out.println(err.toString());
            return null;
        }
    }

    private byte[] classToByteArray() throws URISyntaxException, FileNotFoundException {

        InputStream inputStream;

        //Load the .class in File and open an InputStream
        File fileInput = new File(clazz.getResource(clazz.getSimpleName() + ".class").toURI());
        inputStream = new FileInputStream(fileInput);

        //Stream the File data into a bytes array
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byte[] buffer;
        int nextValue = 0;
        try {
            while ( (nextValue = inputStream.read()) != -1 ) {
                byteStream.write(nextValue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        buffer = byteStream.toByteArray();

        return buffer;

    }




    private class JavaClassLoader extends ClassLoader {

        private byte[] data;

        public JavaClassLoader(byte[] data) {
            this.data = data;
        }

        public Class loadClass() {
            return defineClass(clazz.getName(), data, 0, data.length);
        }


    }
}
