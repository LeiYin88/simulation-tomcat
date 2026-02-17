package org.example.mytomcat.servlet;

import org.example.mytomcat.HttpRequest;
import org.example.mytomcat.HttpResponse;

import java.io.IOException;

/**
 * <p>servlet规范中的类</p>
 * <p>以下是使用示例，使用该注解用于标注其是一个servlet</p>
 * 在容器启动时，会扫描并实例化该类，使之可以通过HTTP协议访问
 * <pre>
 * {@code
 * @WebServlet("/test")
 * public class TestServlet extend Servlet{
 * }}
 * </pre>
 * @see org.example.mytomcat.Tomcat
 * @see org.example.mytomcat.servlet.ClassLoaderServletClassScanner
 * @see org.example.mytomcat.annotation.InitParam
 * @see org.example.mytomcat.annotation.WebServlet
 * @author Yin
 * @since 2023/12/20 11:25 星期二
 * @version 1.1
 */
public interface Servlet {
    void init(ServletConfig config);

    ServletConfig getServletConfig();

    void service(HttpRequest var1, HttpResponse var2) throws IOException;

    String getServletInfo();

    void destroy();
}
