package org.example.mytomcat.servlet;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * <p>容器启动时扫描器实现的接口规范</p>
 * @author Yin
 * @since 2023/12/20 11:36 星期二
 * @version 1.1
 */
public interface ServletClassScanner {
    void scan();

    Map<File, ServletContext> getServletContexts();
}
