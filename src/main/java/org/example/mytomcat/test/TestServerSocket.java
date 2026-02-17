package org.example.mytomcat.test;

import java.net.Socket;

public class TestServerSocket {
    public static void main(String[] args) throws Exception{
        byte[] buffer = new byte[42];
        Socket socket = new Socket("localhost", 8888);
        socket.getOutputStream().write("asdf".getBytes());

//        int len = socket.getInputStream().read();
//        System.out.println(len);

        int len = socket.getInputStream().read(buffer);
        System.out.println(new String(buffer, 0, len));

        System.out.println(111);

        Socket socket2 = new Socket("localhost", 8888);
        socket2.getOutputStream().write("asdf".getBytes());

//        int len = socket.getInputStream().read();
//        System.out.println(len);

        int len2 = socket2.getInputStream().read(buffer);
        System.out.println(new String(buffer, 0, len2));

        //不让程序终止，让Socket read超时异常发生
        Thread.sleep(15000);
    }
}
