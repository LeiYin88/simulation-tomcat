package org.example.mytomcat.test;

import org.example.mytomcat.annotation.WebServlet;
import org.springframework.core.annotation.AnnotationUtils;

import java.io.File;

public class TestScan {
    public static void main(String[] args) throws ClassNotFoundException {
        //此种方法需要后面自己去指定-cp
        String baseDir = System.getProperty("user.dir");
        String webappsDir = baseDir + "\\ROOT\\WEB-INF\\classes";
        System.out.println(webappsDir);
        File file = new File(webappsDir + "MyServlet.class");

        //先用可加载的类测试
//        Class<?> myServlet = ClassLoader.getSystemClassLoader().loadClass("org.example.mytomcat.test.MyServlet");
//        System.out.println(myServlet);

        WebServlet annotation = AnnotationUtils.findAnnotation(TestScan.class, WebServlet.class);
        System.out.println(annotation);
    }
}
