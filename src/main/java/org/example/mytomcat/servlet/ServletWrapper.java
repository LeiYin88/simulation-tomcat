package org.example.mytomcat.servlet;

/**
 * <p>对实例化的Servlet进行一层包装</p>
 * @author Yin
 * @since 2023/12/20 11:36 星期二
 * @version 1.1
 */
public class ServletWrapper {
    private Servlet servlet;
    private String urlPattern;
    private ServletConfig config;
    private Class<? extends Servlet> clazz;

    public Class<? extends Servlet> getClazz() {
        return clazz;
    }

    public void setClazz(Class<? extends Servlet> clazz) {
        this.clazz = clazz;
    }

    public Servlet getServlet() {
        return servlet;
    }

    public void setServlet(Servlet servlet) {
        this.servlet = servlet;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    public ServletConfig getConfig() {
        return config;
    }

    public void setConfig(ServletConfig config) {
        this.config = config;
    }

    @Override
    public String toString() {
        return "ServletWrapper{" +
                "servlet=" + servlet +
                ", urlPattern='" + urlPattern + '\'' +
                ", config=" + config +
                ", clazz=" + clazz +
                '}';
    }
}
