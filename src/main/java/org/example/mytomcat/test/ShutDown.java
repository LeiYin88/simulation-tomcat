package org.example.mytomcat.test;

import java.net.Socket;

public class ShutDown {
    public static void main(String[] args) throws Exception{
        Socket socket = new Socket("localhost", 8888);
        socket.getOutputStream().write("stop".getBytes());
        socket.close();
    }
}
