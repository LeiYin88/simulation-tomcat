package org.example.mytomcat.test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TestChromeSocket {
    public static void main(String[] args) throws IOException {
        byte[] buffer = new byte[1024];
        ServerSocket serverSocket = new ServerSocket(8088);
        Socket socket = serverSocket.accept();
        int len = socket.getInputStream().read(buffer);
        System.out.println(new String(buffer, 0, len));

        socket.getOutputStream().write("HTTP/1.1 200 OK\r\nConnection: close\r\nContent-Length: 0\r\n\r\n".getBytes(StandardCharsets.UTF_8));

        long start = System.currentTimeMillis();
        while(socket.getInputStream().read() != -1){

        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        socket.close();
    }
}
