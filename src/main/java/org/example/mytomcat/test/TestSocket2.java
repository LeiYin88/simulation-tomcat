package org.example.mytomcat.test;

import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TestSocket2 {
    public static void main(String[] args) throws Exception {
        byte[] buffer = new byte[1024];
        Socket socket = new Socket("localhost", 8081);
        socket.getOutputStream().write("GET / HTTP/1.1\r\n\r\n".getBytes(StandardCharsets.UTF_8));
        int len = socket.getInputStream().read(buffer);
        System.out.println(new String(buffer, 0, len, StandardCharsets.UTF_8));

        //不关闭socket，模拟程序异常终止的情况，观察服务器情况

        //不让程序异常终止，让read超时异常发生
        Thread.sleep(15000);
    }
}
