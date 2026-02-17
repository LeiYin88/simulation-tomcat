package org.example.mytomcat;

import org.example.mytomcat.servlet.ServletContext;
import org.example.mytomcat.servlet.ServletWrapper;

import java.io.File;
import java.util.Map;

/**
 * @author Yin
 * @since 2023/12/20 11:28 星期二
 * @version 1.1
 */
public interface Context {
    void start();

    ServletWrapper getDefaultServletWrapper();

    Map<File, ServletContext> getServletContexts();
}
