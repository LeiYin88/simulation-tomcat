package org.example.mytomcat.servlet;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.example.mytomcat.Constants.SERVER_NAME;

/**
 * <p>实现</p>
 * @see  ServletContext
 * @author Yin
 * @since 2023/12/20 11:36 星期二
 * @version 1.1
 */
public class ServletContextImpl implements ServletContext{
    //项目上下文
    private String contextPath;
    //项目中的class
    private List<Class<?>> classes = new ArrayList<>();
    //项目中的servlet
    private List<ServletWrapper> wrappers = new ArrayList<>();

    @Override
    public String getContextPath() {
        return contextPath;
    }

    @Override
    public ServletContext getContext(String var1) {
        return this;
    }

    @Override
    public String getServerInfo() {
        return SERVER_NAME + "\nauthor:Yin";
    }

    @Override
    public String getInitParameter(String var1) {
        return null;
    }

    @Override
    public Set<String> getInitParameterNames() {
        return null;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public List<Class<?>> getClasses() {
        return classes;
    }

    public List<ServletWrapper> getWrappers() {
        return this.wrappers;
    }

    @Override
    public String toString() {
        return "ServletContextImpl{" +
                "contextPath='" + contextPath + '\'' +
                ", classes=" + classes +
                '}';
    }
}
