package org.example.mytomcat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Yin
 * @since 2023/12/20 11:28 星期二
 * @version 1.1
 */
public class HttpRequest {
    //头部字段
    protected Map<String, String> header = new ConcurrentHashMap<>();
    //请求参数
    protected Map<String, String> parameters = new ConcurrentHashMap<>();
    //请求体
    protected byte[] body;

    public String getHeader(String key){
        return header.get(key);
    }

    public Map<String, String> getParameters(){
        return parameters;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
