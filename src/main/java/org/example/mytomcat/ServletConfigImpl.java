package org.example.mytomcat;

import org.example.mytomcat.servlet.ServletConfig;
import org.example.mytomcat.servlet.ServletContext;

import java.util.Map;
import java.util.Set;

/**
 * @author Yin
 * @since 2023/12/20 11:28 星期二
 * @version 1.1
 */
public class ServletConfigImpl implements ServletConfig {
    private String servletName;
    private Map<String, String> initParameters;
    private Set<String> initParameterNames;
    private ServletContext servletContext;

    public ServletConfigImpl() {
    }

    @Override
    public String getServletName() {
        return servletName;
    }

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    protected void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public String getInitParameter(String key) {
        return initParameters.get(key);
    }

    @Override
    public Set<String> getInitParameterNames() {
        return initParameterNames;
    }

    public void setServletName(String servletName) {
        this.servletName = servletName;
    }

    public void setInitParameters(Map<String, String> initParameters) {
        this.initParameters = initParameters;
        initParameterNames = initParameters.keySet();
    }

    @Override
    public String toString() {
        return "ServletConfigImpl{" +
                "servletName='" + servletName + '\'' +
                ", initParameters=" + initParameters +
                ", initParameterNames=" + initParameterNames +
                '}';
    }
}
