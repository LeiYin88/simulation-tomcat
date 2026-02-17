package org.example.mytomcat.servlet;

import org.example.mytomcat.HttpRequest;
import org.example.mytomcat.HttpResponse;
import org.example.mytomcat.Log;
import org.example.mytomcat.exception.NotFoundContextPathException;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import static org.example.mytomcat.Constants.*;


/**
 * <p>处理静态资源，调度其他servlet</p>
 * @see org.example.mytomcat.servlet.Servlet
 * @author Yin
 * @since 2023/12/20 11:35 星期二
 * @version 1.1
 */
public class DefaultServlet implements Servlet{
    private Map<File, ServletContext> servletContexts;
    private ServletConfig servletConfig;

    private ServletContext currServletContext;

    private String webapps = TOMCAT_HOME + "\\" + STANDARD_DIR_WABAPPS + "\\";


    public void setServletContexts(Map<File, ServletContext> servletContexts) {
        this.servletContexts = servletContexts;
    }

    @Override
    public void init(ServletConfig config) {
        this.servletConfig = config;
        Log.infor(" " + config.getServletName() + " initialized", String.valueOf(DEFAULT_TOMCAT_PORT));
    }

    @Override
    public ServletConfig getServletConfig() {
        return servletConfig;
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        //获取请求的url
        String url = request.getHeader(URL_HEADER);
        Log.debug(" current request url: " + url, String.valueOf(port));

        //截取掉参数部分，才是真实的url，参数部分在此处已经被封装到了request对象中
        int index = url.indexOf("?");
        String realUrl = url.substring(0, (index == -1 ? url.length() : index));

        Log.debug(" current request realUrl: " + realUrl, String.valueOf(port));

        //解析contextPath
        String contextPath = parseContextPath(realUrl);
        //确定当前的项目上下文
        determineServletContext(contextPath);
        if (currServletContext == null){
            //如果webapps中没有项目，那么响应存在于webapps及其子目录中的静态资源
            //响应静态资源，如果没有找到，则返回404
            String filePath =  webapps + realUrl.replace("/","\\");
            //将静态资源写入响应体
            handleStaticResource(response.getBody(), filePath, realUrl);
            return;
        }
        //遍历所有的servlet，看看是否可以处理
        ServletWrapper wrapper;
        if ((wrapper = determineServletWrapper(contextPath, realUrl)) != null){
            //不为null，则说明找到了servlet处理，创建servlet实例
            if (wrapper.getServlet() == null) {
                try {
                    newInstance(wrapper);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Servlet servlet = wrapper.getServlet();
            //调用对应的servlet处理
            servlet.service(request, response);
            return;
        }
        //为null
        //进一步处理，判断响应静态资源还是404页面
        if (url.contains(STANDARD_DIR_WEB_INF)){
            //WEB-INF中的静态资源不允许请求
            //抛出该异常，让acceptor响应404页面
            throw new NotFoundContextPathException();
        }
        //如果webapps中存在项目，那么则不会走到上面的响应静态资源的判断中，此处需要再次响应一次静态资源
        //此逻辑的问题：如果servlet中的url-pattern与静态资源的url一致，那么会走到servlet中而不是响应静态资源
        String filePath =  webapps + realUrl.replace("/","\\");
        //将静态资源写入响应体
        handleStaticResource(response.getBody(), filePath, realUrl);
    }

    private void handleStaticResource(ByteArrayOutputStream out, String path, String url) throws IOException {
        FileInputStream in = null;
        try {
            in = new FileInputStream(path);
            int len = 0;
            while ((len = in.read()) != -1) {
                out.write(len);
            }
        }catch (FileNotFoundException e){
            Log.error(" can not found " + url + " resource", String.valueOf(port));
            //找不到该静态资源，则抛异常显示404页面
            throw new NotFoundContextPathException();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void newInstance(ServletWrapper wrapper) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Servlet servlet = wrapper.getClazz().getConstructor().newInstance();
        servlet.init(wrapper.getConfig());
        wrapper.setServlet(servlet);
    }

    private ServletWrapper determineServletWrapper(String contextPath, String url){
        List<ServletWrapper> wrappers = currServletContext.getWrappers();
        for (ServletWrapper wrapper : wrappers) {
            String urlPattern = wrapper.getUrlPattern();
            String servletUrl = "/" + contextPath + urlPattern;
            if (servletUrl.equals(url)){
                return wrapper;
            }
        }
        return null;
    }

    private void determineServletContext(String contextPath) {
        servletContexts.forEach((file, servletContext) -> {
            if (servletContext.getContextPath().equals(contextPath)){
                currServletContext = servletContext;
            }
        });
    }

    public String parseContextPath(String url){
        //正确的url开头为/ 故从1开始解析
        int i = 1;
        char[] str = url.toCharArray();
        while(i < url.length()){
            if (str[i] == '/'){
                return new String(str, 1, i - 1);
            }
            i++;
        }
        return url.substring(1);
    }

    @Override
    public String getServletInfo() {
        return this.toString();
    }

    @Override
    public void destroy() {
        Log.infor(" " + getServletConfig().getServletName() + " destroy", String.valueOf(DEFAULT_TOMCAT_PORT));
    }
}
