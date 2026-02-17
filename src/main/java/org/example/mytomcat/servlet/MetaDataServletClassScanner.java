package org.example.mytomcat.servlet;

import java.io.File;
import java.util.Map;

/**
 * <p>todo</p>
 * @author Yin
 * @since 2023/12/20 11:36 星期二
 * @version 1.1
 */
public class MetaDataServletClassScanner implements ServletClassScanner{
    @Override
    public void scan() {
        //todo 用不加载类的方式来判断是否存在某个注解

    }

    @Override
    public Map<File, ServletContext> getServletContexts() {
        return null;
    }
}
