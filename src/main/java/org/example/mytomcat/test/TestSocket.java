package org.example.mytomcat.test;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

//此类用户测试给服务端发送信息
public class TestSocket {
    public static void main(String[] args) throws IOException, InterruptedException {
        byte[] buffer = new byte[123];
        Socket socket = new Socket("localhost", 8081);
        System.out.println("");
        Thread.sleep(3);

        socket.close();

        socket.getOutputStream().write("GET / HTTP/1.1\r\n\r\n".getBytes(StandardCharsets.UTF_8));
//        socket.getOutputStream().write(("GET / HTTP/1.1\r\n" +
//                "Host: localhost:8080\r\n" +
//                "Content-Type: application/json;charset=utf-8\r\n" +
//                "Content-Length: 43\r\n" +
//                "Connection: keep-alive\r\n" +
//                "\r\n" +
//                "{\"username\":\"zhangsan\",\"password\":\"123456\"}").getBytes(StandardCharsets.UTF_8));

        socket.getInputStream().read(buffer);

        System.out.println(new String(buffer, StandardCharsets.UTF_8));

//        Thread.sleep(5000);
//
//        //无法再次发送第二次
//        socket.getOutputStream().write(("GET / HTTP/1.1\r\n" +
//                "Host: localhost:8080\r\n" +
//                "Connection: keep-alive\r\n" +
//                "Pragma: no-cache\r\n" +
//                "Cache-Control: no-cache\r\n" +
//                "Upgrade-Insecure-Requests: 1\r\n" +
//                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36\r\n" +
//                "Sec-Fetch-User: ?1\r\n" +
//                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9\r\n" +
//                "Sec-Fetch-Site: none\r\n" +
//                "Sec-Fetch-Mode: navigate\r\n" +
//                "Accept-Encoding: gzip, deflate, br\r\n" +
//                "Accept-Language: zh-CN,zh;q=0.9\r\n" +
//                "Cookie: Idea-5546530b=e4dcfa82-2324-426c-b42e-af5376cfe072; " +
//                "Webstorm-7a1e53d2=f42d3928-bd8a-4355-902f-02b6905c8b55; " +
//                "JSESSIONID=54EC78DA5C1E7BA94F6AF54AE71B5ABD\r\n\r\n").getBytes(StandardCharsets.UTF_8));
//
//
//        socket.getInputStream().read(buffer);
//
//        System.out.println(new String(buffer, StandardCharsets.UTF_8));

        socket.close();
    }
}
