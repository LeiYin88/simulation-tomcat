package org.example.mytomcat.test;

import java.net.Socket;

public class TestCloseSocket {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 8080);

        Thread.sleep(100000);
    }
}
