package org.example.mytomcat.servlet;

import java.util.List;
import java.util.Set;

/**
 * <p>servlet规范中的类</p>
 * @author Yin
 * @since 2023/12/20 11:36 星期二
 * @version 1.1
 */
public interface ServletContext {
    String getContextPath();

    ServletContext getContext(String var1);

    String getServerInfo();

    String getInitParameter(String var1);

    Set<String> getInitParameterNames();

    List<Class<?>> getClasses();

    List<ServletWrapper> getWrappers();
}
