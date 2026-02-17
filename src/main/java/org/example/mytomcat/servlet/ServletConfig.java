package org.example.mytomcat.servlet;

import java.util.Set;

/**
 * <p>servlet规范中的类</p>
 * 利用这种方式能获取到当前servlet的配置信息
 * <pre>
 * {@code
 * @WebServlet("/test")
 * public class TestServlet extend Servlet{
 *    private ServletConfig servletConfig;
 *
 *     public void init(ServletConfig config) {
 *         this.config = config;
 *     }
 *
 *    public ServletConfig getServletConfig() {
 *         return this.config;
 *     }
 * }}
 * @author Yin
 * @since 2023/12/20 11:36 星期二
 * @version 1.1
 */
public interface ServletConfig {
    String getServletName();

    ServletContext getServletContext();

    String getInitParameter(String var1);

    Set<String> getInitParameterNames();
}
