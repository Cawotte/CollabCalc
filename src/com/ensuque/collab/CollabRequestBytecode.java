package com.ensuque.collab;

import java.io.*;
import java.net.URISyntaxException;

/**
 * CollabRequest using Bytecode instead of an object. Load the class into a bytearray and unserialize it later
 * back into a class. DOESN'T require a serializable class like CollabRequestObject.
 * <p/>
 * Will instantiate an instance of the object using the class, however will ALWAYS use the default Constructor.
 * @param <T>
 */
public class CollabRequestBytecode<T> extends CollabRequest implements Serializable {

    //Byte array in which the .class will be serialized into.
    private byte[] byteArray;

    public CollabRequestBytecode(Class<T> clazz, String methodName, Object[] args)
            throws NoSuchMethodException, InvalidReturnType, FileNotFoundException, URISyntaxException {
        super(clazz, methodName, args);

        this.byteArray = classToByteArray();
    }

    @Override
    protected T getObjectInstance() throws Exception {

        //Load the byte array into a class
        CollabClassLoader _classLoader = new CollabClassLoader(byteArray);
        Class regeneratedClass = _classLoader.loadClass();

        return (T)regeneratedClass.getConstructor().newInstance();
    }

    /**
     * Serialize the .class file into a byte array.
     * @return
     * @throws URISyntaxException
     * @throws FileNotFoundException
     */
    private byte[] classToByteArray() throws URISyntaxException, FileNotFoundException {


        //Load the .class in File and open an InputStream
        File fileInput = new File(clazz.getResource(clazz.getSimpleName() + ".class").toURI());
        InputStream inputStream = new FileInputStream(fileInput);

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

    /**
     * Custom Class Loader required to turns the byte array back into a working class.
     */
    private class CollabClassLoader extends ClassLoader {

        private byte[] data;

        CollabClassLoader(byte[] data) {
            this.data = data;
        }

        Class loadClass() {
            return defineClass(clazz.getName(), data, 0, data.length);
        }


    }
}
