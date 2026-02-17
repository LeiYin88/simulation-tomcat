package org.example.mytomcat.test;

import org.example.mytomcat.HttpRequest;
import org.example.mytomcat.HttpResponse;
import org.example.mytomcat.annotation.InitParam;
import org.example.mytomcat.annotation.WebServlet;
import org.example.mytomcat.servlet.GenericServlet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@WebServlet(value = "/hello", initParams = {@InitParam(initName = "username", initValue = "zhangsan")}, servletName = "helloServlet")
public class HelloServlet extends GenericServlet {

    @Override
    public void init() {
        System.out.println(getServletConfig());
        System.out.println(getServletConfig().getServletName());
        System.out.println(getServletConfig().getInitParameterNames());
        System.out.println(getServletConfig().getInitParameter("username"));
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        Map<String, String> parameters = request.getParameters();
        System.out.println(parameters);

        System.out.println("HelloServlet...");
        response.setHeader("Content-Type","text/plain;charset=utf-8");
        response.getBody().write("<h1>Hello Servlet Service...</h1>".getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void destroy() {
        System.out.println("destroy 111");
    }
}
