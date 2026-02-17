package org.example.mytomcat.servlet;


import org.example.mytomcat.HttpRequest;
import org.example.mytomcat.HttpResponse;

import java.io.IOException;
import java.io.Serializable;

import java.util.Set;

/**
 * <p>适配器，可以通过继承该抽象类作为一个servlet</p>
 * @see org.example.mytomcat.servlet.Servlet
 * @author Yin
 * @since 2023/12/20 11:35 星期二
 * @version 1.1
 */
public abstract class GenericServlet implements Servlet, ServletConfig, Serializable {
    private transient ServletConfig config;

    public GenericServlet() {
    }

    public void destroy() {
    }

    public String getInitParameter(String name) {
        ServletConfig sc = this.getServletConfig();
        if (sc == null) {
            throw new IllegalStateException("err.servlet_config_not_initialized");
        } else {
            return sc.getInitParameter(name);
        }
    }

    public Set<String> getInitParameterNames() {
        ServletConfig sc = this.getServletConfig();
        if (sc == null) {
            throw new IllegalStateException("err.servlet_config_not_initialized");
        } else {
            return sc.getInitParameterNames();
        }
    }

    public ServletConfig getServletConfig() {
        return this.config;
    }

    public ServletContext getServletContext() {
        ServletConfig sc = this.getServletConfig();
        if (sc == null) {
            throw new IllegalStateException("err.servlet_config_not_initialized");
        } else {
            return sc.getServletContext();
        }
    }

    public String getServletInfo() {
        return "";
    }

    public void init(ServletConfig config) {
        this.config = config;
        this.init();
    }

    public void init() {
    }

    public abstract void service(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException;

    public String getServletName() {
        ServletConfig sc = this.getServletConfig();
        if (sc == null) {
            throw new IllegalStateException("err.servlet_config_not_initialized");
        } else {
            return sc.getServletName();
        }
    }
}
