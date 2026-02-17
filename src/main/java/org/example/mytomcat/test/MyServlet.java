package org.example.mytomcat.test;

import org.example.mytomcat.HttpRequest;
import org.example.mytomcat.HttpResponse;
import org.example.mytomcat.annotation.WebServlet;
import org.example.mytomcat.servlet.Servlet;
import org.example.mytomcat.servlet.ServletConfig;

import java.io.IOException;

@WebServlet(value = "/a")
public class MyServlet implements Servlet {

    @Override
    public void init(ServletConfig config) {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(HttpRequest var1, HttpResponse var2) throws IOException {
        System.out.println("Hello, Servlet");
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
