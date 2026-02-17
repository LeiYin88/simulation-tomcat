package org.example.mytomcat;

import org.example.mytomcat.annotation.InitParam;
import org.example.mytomcat.annotation.WebServlet;
import org.example.mytomcat.servlet.*;
import org.springframework.core.annotation.AnnotationUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.example.mytomcat.Constants.DEFAULT_SERVLET_NAME;
import static org.example.mytomcat.Constants.DEFAULT_SERVLET_URL_PATTERN;

/**
 * @author Yin
 * @since 2023/12/20 11:28 星期二
 * @version 1.1
 */
public class StandardContext implements Context {
    //所有的应用上下文
    Map<File, ServletContext> servletContexts;
    //默认servlet全局共享
    private ServletWrapper defaultServletWrapper;

    ServletClassScanner classScanner = new ClassLoaderServletClassScanner();

    @Override
    public void start() {
        servletContexts = classScanner.getServletContexts();
        newDefaultInstance();
        newInstances(servletContexts);
    }


    private void newInstances(Map<File, ServletContext> servletContexts) {
        servletContexts.forEach((file, servletContext) -> {
            //获取每个上下文中的class
            List<Class<?>> classes = servletContext.getClasses();
            List<ServletWrapper> wrappers = servletContext.getWrappers();
            //分别初始化
            for (Class<?> clazz : classes) {
                //创建servlet
                WebServlet webServlet = AnnotationUtils.findAnnotation(clazz, WebServlet.class);
                if (webServlet != null) {
                    Map<String, String> initProperty = new HashMap<>();
                    String value = webServlet.value();
                    String servletName = webServlet.servletName();
                    InitParam[] initParams = webServlet.initParams();
                    for (InitParam initParam : initParams) {
                        String initName = initParam.initName();
                        String initValue = initParam.initValue();
                        initProperty.put(initName, initValue);
                    }
                    newWrapperInstance(servletContext, initProperty, value, servletName, clazz, wrappers);
                }
            }
        });
    }

    private void newWrapperInstance(
            ServletContext servletContext,
            Map<String, String> initData,
            String urlPattern,
            String servletName,
            Class clazz,
            List<ServletWrapper> wrappers) {
        //todo new ServletWrapper
        ServletConfigImpl config = new ServletConfigImpl();
        config.setServletName(servletName);
        config.setInitParameters(initData);
        config.setServletContext(servletContext);
        ServletWrapper servletWrapper = new ServletWrapper();
        servletWrapper.setConfig(config);
        servletWrapper.setUrlPattern(urlPattern);
        servletWrapper.setClazz(clazz);
        wrappers.add(servletWrapper);
    }

    private void newDefaultInstance() {
        //写死，不允许用户修改配置
        ServletConfigImpl config = new ServletConfigImpl();
        config.setServletName(DEFAULT_SERVLET_NAME);
        ServletWrapper servletWrapper = new ServletWrapper();
        servletWrapper.setConfig(config);
        servletWrapper.setUrlPattern(DEFAULT_SERVLET_URL_PATTERN);
        servletWrapper.setClazz(DefaultServlet.class);
        this.defaultServletWrapper = servletWrapper;
    }

    public ServletWrapper getDefaultServletWrapper() {
        return defaultServletWrapper;
    }

    public Map<File, ServletContext> getServletContexts() {
        return servletContexts;
    }
}
