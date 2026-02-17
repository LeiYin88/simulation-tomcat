package org.example.mytomcat.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>servlet规范中的注解</p>
 * <p>以下是使用示例，使用该注解用于标注其是一个servlet</p>
 * 在容器启动时，会扫描并实例化该类，使之可以通过HTTP协议访问
 *  <pre>
 *  {@code
 *  @WebServlet("/test")
 *  public class TestServlet extend Servlet{
 *  }}
 *  </pre>
 * @see org.example.mytomcat.Tomcat
 * @see org.example.mytomcat.servlet.ClassLoaderServletClassScanner
 * @author Yin
 * @since 2023/12/20 11:25 星期二
 * @version 1.1
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WebServlet {
    //url-pattern
    String value();

    String servletName() default "";

    InitParam[] initParams() default {};
}
