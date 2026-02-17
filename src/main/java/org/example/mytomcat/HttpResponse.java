package org.example.mytomcat;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Yin
 * @since 2023/12/20 11:28 星期二
 * @version 1.1
 */
public class HttpResponse {
    protected Map<String, String> header = new ConcurrentHashMap<>();
    //留给用户写
    protected ByteArrayOutputStream body = new ByteArrayOutputStream();

    public String setHeader(String k, String v){
        return header.put(k,v);
    }

    public ByteArrayOutputStream getBody() {
        return body;
    }

    public Map<String, String> getHeader() {
        return header;
    }
}
