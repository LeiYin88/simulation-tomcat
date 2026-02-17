package org.example.mytomcat.servlet;

import org.example.mytomcat.Constants;
import org.example.mytomcat.Log;
import org.example.mytomcat.WebAppClassLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import static org.example.mytomcat.Constants.*;

/**
 *
 *
 * @deprecated : <p>加载类到jvm，通过反射的方式判断</p>当类过多时，会加重jvm内存负担
 * @author Yin
 * @since 2023/12/20 11:34 星期二
 * @version 1.1
 */

@Deprecated
public class ClassLoaderServletClassScanner implements ServletClassScanner{
    private Map<File, ServletContext> servletContexts = new ConcurrentHashMap<>();
    private WebAppClassLoader webAppClassLoader;
    protected static final String prefix = "\\" + STANDARD_DIR_WABAPPS;
    protected static final String suffix = "\\" + STANDARD_DIR_WABAPPS + "\\" + STANDARD_DIR_CLASSES;
    public static final String baseDir;
    private String baseScanDir;
    private List<File> needScanDir = new ArrayList<>();
    private List<String> classesPath = new ArrayList<>();
    private Map<String, String> classes = new HashMap<>();
    private Map<String, Class<?>> clazzs = new HashMap<>();

    static {
        baseDir = Constants.TOMCAT_HOME;
    }

    public ClassLoaderServletClassScanner() {
        baseScanDir = baseDir + prefix;
        log();
        getNeedScanDir();
        webAppClassLoader = new WebAppClassLoader(null);
    }

    private void log() {
        Log.infor(" start scan inner webapps servlet", String.valueOf(Constants.port));
    }

    private void getNeedScanDir(){
        File file = new File(baseScanDir);
        if (!file.isDirectory()) {
            throw new NullPointerException("找不到" + STANDARD_DIR_WABAPPS + "目录");
        }
        File[] webapps = file.listFiles();
        for (File webapp : webapps) {
            if (webapp.isDirectory()) {
                File[] f = webapp.listFiles();
                //创建上下文对象
                ServletContextImpl servletContext = new ServletContextImpl();
                servletContext.setContextPath(webapp.getName());
                for (File ff : f) {
                    if (ff.isDirectory() && ff.getName().equals(STANDARD_DIR_WEB_INF)){
                        File[] fff = ff.listFiles();
                        for (File ffff : fff) {
                            if (ffff.isDirectory() && ffff.getName().equals(STANDARD_DIR_CLASSES)){
                                needScanDir.add(ffff);
                                File[] fs = ffff.listFiles();
                                if (fs != null) {
                                    for (File isClazz : fs) {
                                        if (isClazz.isFile() && isClazz.getAbsolutePath().endsWith(".class")){
                                            classesPath.add(isClazz.getAbsolutePath());
                                        }
                                        if (isClazz.isDirectory()){
                                            //说明是类的包
                                            //递归扫描类
                                            scanPackage(isClazz);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                servletContexts.put(webapp, servletContext);
            }
        }
    }

    private void scanPackage(File pack){
        File[] files = pack.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    scanPackage(file);
                }else {
                    classesPath.add(file.getAbsolutePath());
                }
            }
        }
    }

    @Override
    public void scan() {
        if (!needScanDir.isEmpty() && !classesPath.isEmpty()) {
            PathToClassName(classesPath);
            if (!classes.isEmpty()){
                classes.forEach((path, clazzName) -> {
                    webAppClassLoader.setPath(path);
                    Class<?> clazz = null;
                    try {
                        clazz = webAppClassLoader.loadClass(clazzName);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (clazz != null){
                        //重新添加集合元素
                        clazzs.put(path, clazz);
                    }
                });
            }
        }
    }

    @Override
    public Map<File, ServletContext> getServletContexts() {
        scan();
        Log.infor(" scanned out context: " + ((Supplier<String>) () -> {
            StringBuilder sb = new StringBuilder();
            servletContexts.keySet().forEach(file -> {
                sb.append(file.getName()).append(" ");
            });
            return String.format("\033[%dm%s\033[0m", 35, sb);
        }).get(), String.valueOf(Constants.port));
        //分类
        sort(servletContexts, clazzs);
        return servletContexts;
    }

    private void sort(Map<File, ServletContext> servletContexts, Map<String, Class<?>> classes) {
        servletContexts.forEach((contextPath, servletContext) -> {
            classes.forEach((classPath, clazz) -> {
                //如果是同一个项目中的，则将该class添加到该上下文的classes集合中
                //fixme 可能会出现问题，需要注意，此处用文件的绝对路径中是否包含目录的绝对路径来判断是否是该项目中的class
                if (classPath.contains(contextPath.getAbsolutePath())){
                    servletContext.getClasses().add(clazz);
                }
            });
        });
    }

    private void PathToClassName(List<String> classesPath) {
        for (String path : classesPath) {
            int start = path.indexOf(STANDARD_DIR_CLASSES);
            int ent = path.indexOf(".class");
            String className = path.substring(start + STANDARD_DIR_CLASSES.length() + 1, ent).replace("\\", ".");
            classes.put(path, className);
        }
    }


    public static void main(String[] args) {
        ClassLoaderServletClassScanner classLoaderServletClassScanner = new ClassLoaderServletClassScanner();
        Map<File, ServletContext> servletContexts = classLoaderServletClassScanner.getServletContexts();
        System.out.println(servletContexts);
    }
}
