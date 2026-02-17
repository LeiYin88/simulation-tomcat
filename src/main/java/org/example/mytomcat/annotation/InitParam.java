package org.example.mytomcat.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>servlet规范中的注解</p>
 * <pre>
 * {@code
 * @WebServlet(@InitParam())
 * public class TestServlet extend Servlet{
 * }}
 * </pre>
 * @author Yin
 * @since 2023/12/20 11:25 星期二
 * @version 1.1
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface InitParam {
    String initName();

    String initValue();
}
