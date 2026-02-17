package org.example.mytomcat.test;

import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client {
    public static void main(String[] args) throws Exception{
        byte[] bytes = new byte[8912];

        Socket socket = new Socket("localhost", 8081);
        socket.getOutputStream().write(("GET /ROOT/test.html HTTP/1.1\r\n" +
                "Host: localhost:8081\r\n" +
                "Connection: keep-alive\r\n" +
                "Pragma: no-cache\r\n" +
                "Cache-Control: no-cache\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "Accept-Language: zh-CN,zh;q=0.9\r\n" +
                "Cookie: Idea-5546530b=e4dcfa82-2324-426c-b42e-af5376cfe072; Webstorm-7a1e53d2=f42d3928-bd8a-4355-902f-02b6905c8b55\r\n\r\n").getBytes(StandardCharsets.UTF_8));

        int len = socket.getInputStream().read(bytes);

        System.out.println(new String(bytes, 0, len));

        socket.getOutputStream().write(("GET /ROOT/a.js HTTP/1.1\r\n" +
                "Host: localhost:8081\r\n" +
                "Connection: keep-alive\r\n" +
                "Pragma: no-cache\r\n" +
                "Cache-Control: no-cache\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "Accept-Language: zh-CN,zh;q=0.9\r\n" +
                "Cookie: Idea-5546530b=e4dcfa82-2324-426c-b42e-af5376cfe072; Webstorm-7a1e53d2=f42d3928-bd8a-4355-902f-02b6905c8b55\r\n\r\n").getBytes(StandardCharsets.UTF_8));

        int len2 = socket.getInputStream().read(bytes);

        System.out.println(new String(bytes, 0, len2));

        Socket socket1 = new Socket("localhost", 8081);

        socket1.getOutputStream().write(("GET /ROOT/a.css HTTP/1.1\r\n" +
                "Host: localhost:8081\r\n" +
                "Connection: keep-alive\r\n" +
                "Pragma: no-cache\r\n" +
                "Cache-Control: no-cache\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "Accept-Language: zh-CN,zh;q=0.9\r\n" +
                "Cookie: Idea-5546530b=e4dcfa82-2324-426c-b42e-af5376cfe072; Webstorm-7a1e53d2=f42d3928-bd8a-4355-902f-02b6905c8b55\r\n\r\n").getBytes(StandardCharsets.UTF_8));

        int len3 = socket1.getInputStream().read(bytes);

        System.out.println(new String(bytes, 0, len3));

        socket.close();
        socket1.close();

    }
}
