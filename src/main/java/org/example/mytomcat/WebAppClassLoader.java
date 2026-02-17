package org.example.mytomcat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Yin
 * @since 2023/12/20 11:28 星期二
 * @version 1.1
 */
public class WebAppClassLoader extends ClassLoader { /* 没有打破双亲委派机制 */
    //指定字节码的路径
    private String path ;

    public WebAppClassLoader(String path){
        this.path = path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 重写findClass方法
     * @param name 是我们这个类的全路径
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class clazz = null;
        // 获取该class文件字节码数组
        byte[] classData = getData();

        if (classData != null) {
            // 将class的字节码数组转换成Class类的实例
            clazz = defineClass(name, classData, 0, classData.length);
        }
        return clazz;
    }

    /**
     * 将class文件转化为字节码数组
     * @return
     */
    private byte[] getData() {
        if (path == null){
            return null;
        }
        File file = new File(path);
        if (file.exists()){
            FileInputStream in = null;
            ByteArrayOutputStream out = null;
            try {
                in = new FileInputStream(file);
                out = new ByteArrayOutputStream();

                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = in.read(buffer)) != -1) {
                    out.write(buffer, 0, size);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
            return out.toByteArray();
        }else{
            return null;
        }
    }
}
