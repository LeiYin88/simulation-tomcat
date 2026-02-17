package org.example.mytomcat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.*;
import java.nio.charset.Charset;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

import static org.example.mytomcat.Constants.*;
import static org.example.mytomcat.Tomcat.serverState;

/**
 * <p>连接器</p>
 * @author Yin
 * @since 2023/12/20 11:28 星期二
 * @version 1.1
 */
public class Connector {
    private final int port;

    private Acceptor acceptor;

    private ServerSocket serverSocket;

    public Connector(int port) {
        this.port = port;
    }

    public void start(Context context) {
        acceptor = new Acceptor();
        acceptor.setName("acceptor-thread");
        acceptor.setContext(context);
        acceptor.start(this);
        Log.infor(" listen to " + port, String.valueOf(port));
    }

    public Socket listen() throws Exception {
        //50是ServerSocket源码中的默认值
        return listen(port, 50, null);
    }

    public Socket listen(int backlog) throws Exception {
        return listen(port, backlog, null);
    }

    public Socket listen(int port, int backlog, InetAddress bindAddr) throws Exception {
        if (serverSocket == null) {
            serverSocket = new ServerSocket(port, backlog, bindAddr);
        }
        Socket socket = serverSocket.accept();
        if (socket == null) {
            throw new BindException("cannot connect to target port！");
        }
        return socket;
    }

    public void close() throws IOException {
        Socket socket0 = null;
        //服务器标记
        serverState = true;
        try {
            //如果连接建立成功
            socket0 = new Socket(DEFAULT_HOST, port);
            //标记为关闭
            serverState = false;
            Log.warri(" curr socket mark server state false: " + socket0, String.valueOf(port));
        } catch (IOException e) {
            //如果此时服务器无法接收关闭的请求
            e.printStackTrace();
            throw e;
        }finally {
            if (socket0 != null) {
                try {
                    Log.warri(" close curr socket: " + socket0, String.valueOf(port));
                    socket0.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ThreadPoolExecutor executor = acceptor.getExecutor();
        //等待线程池中的线程处理完毕后关闭
        executor.shutdown();
        //关闭所有的socket
        acceptor.tasks.forEach(SocketWrapper::close);

        acceptor.tasks.clear();
        //销毁servlet
        acceptor.close();
    }


    public Map<HttpRequest, HttpResponse> getMessage(Socket socket) throws IOException {
        if (socket == null) {
            throw new NullPointerException("method arg must not null!");
        }
        InputStream in = socket.getInputStream();

        ByteArrayOutputStream arrOS = new ByteArrayOutputStream();

        if (!parseRequestMessage(in, arrOS, socket)) {
            //进入此处，则表示对方已关闭连接
            //我方也关闭连接
            socket.close();
            return null;
        }

        //将请求行以及请求头中的内容设置到对象中 不包括请求体
        HttpRequest request = createRequest(new HttpRequest(), arrOS, in);
        //创建响应体对象
        HttpResponse response = createResponse(new HttpResponse());

        Map<HttpRequest, HttpResponse> map = new ConcurrentHashMap<>();
        map.put(request, response);
        return map;
    }

    private HttpResponse createResponse(HttpResponse httpResponse) {
        return httpResponse;
    }

    private HttpRequest createRequest(HttpRequest httpRequest, ByteArrayOutputStream arrOS, InputStream in) throws IOException {
        //传输过来的报文会将空白行省略和\n省略，手动添加，用于判断退出循环
        byte[] bytes = arrOS.toByteArray();
        byte[] buf = new byte[bytes.length + 3];
        System.arraycopy(bytes, 0, buf, 0, bytes.length);
        buf[buf.length - 1] = '\n';
        buf[buf.length - 2] = '\r';
        buf[buf.length - 3] = '\n';

        int pos = 0;
        while (true) {
            //请求行
            if (buf[pos] == '\n') {
                String requestLine = new String(buf, 0, pos - 1);
                parseRequestLine(requestLine, httpRequest);
                break;
            }
            pos++;
        }

        /* 解析完请求行，当前指针指向请求行末尾\n */

        /*
            curr pos:
            \nk:v\r\n or \n\r\n
             ↑ pos   or   ↑ pos
         */
        int i = ++pos;
        int end;
        int length;
        /* 解析到空白行 结束解析 */
        while (buf[pos - 1] != '\n' || buf[pos] != '\r') {
            //请求头
            if (buf[pos] == '\n') {
                end = pos - 1;
                length = end - i;
                String partRequestHeader = new String(buf, i, length);
                parseRequestHeader(partRequestHeader, httpRequest);
                i = pos + 1;
            }
            pos++;
        }

        String contentLength = httpRequest.getHeader(CONTENT_LENGTH);
        if (contentLength != null && !"".equals(contentLength)) {
            //只有http请求报文正确，则不会报错，不做异常处理
            int len = Integer.parseInt(contentLength);
            //如果有请求体，则会设置Content-Length字段，需要解析获取该字段用于读取请求体
            //in中的数据只读取到了空白行，有请求体需要继续读取
            parseRequestBody(httpRequest, in, len);
        }
        //设置请求参数
        setRequestParameters(httpRequest);

        return httpRequest;
    }

    private void setRequestParameters(HttpRequest httpRequest) {
        //解析完成报文，将请求的参数设置到请求对象中
        String url = httpRequest.getHeader(URL_HEADER);
        int index = url.indexOf('?');
        //url中携带了参数
        if (index != -1) {
            String paramsStr = url.substring(index + 1);
            String[] params = paramsStr.split("&");
            for (String param : params) {
                String[] kv = param.split("=");
                httpRequest.parameters.put(kv[0], kv[1]);
            }
        }
        //url中没有参数，解析请求体
        byte[] body = httpRequest.body;
        //只解析表单提交的情况
        String contentTypeAndCharSet = httpRequest.getHeader(CONTENT_TYPE);

        if (contentTypeAndCharSet == null || "".equals(contentTypeAndCharSet)) {
            return;
        }

        String[] arr = contentTypeAndCharSet.split(";");
        String contentType = arr[0];
        String charset = "utf-8";

        if (arr.length > 1) {
            charset = arr[1].substring(arr[1].indexOf('=') + 1);
        }

        if (contentType.equalsIgnoreCase(APPLICATION_FORM_DEFAULT)) {
            String formData = new String(body, Charset.forName(charset));
            String[] params = formData.split("&");
            for (String param : params) {
                String[] kv = param.split("=");
                httpRequest.parameters.put(kv[0], kv[1]);
            }
        }
    }

    private void parseRequestHeader(String requestHeader, HttpRequest httpRequest) {
        String[] partHeader = requestHeader.split(":");
        httpRequest.header.put(partHeader[0].trim(), partHeader[1].trim());
    }

    private void parseRequestLine(String requestLine, HttpRequest httpRequest) {
        String[] _method_url_version = requestLine.split(" ");
        httpRequest.header.put(METHOD_HEADER, _method_url_version[0]);
        httpRequest.header.put(URL_HEADER, _method_url_version[1]);
        httpRequest.header.put(VERSION_HEADER, _method_url_version[2]);
    }

    private void parseRequestBody(HttpRequest request, InputStream in, int len) throws IOException {
        //todo 解析请求体 半成品
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int index = 0;
        while (index < len) {
            byte b = (byte) in.read();
            out.write(b);
            index++;
        }
        byte[] body = out.toByteArray();
        //解析参数
        //如果是表单提交，do...
        //如果是文件上传，do...
        //此处直接将读出的字节流设置给用户使用
        request.setBody(body);
    }

    //将请求头的内容读取出来
    public boolean parseRequestMessage(InputStream in, ByteArrayOutputStream out, Socket socket) throws IOException {
        //报文必须为规范http请求报文，否则无法解析，解析会出错
        while (true) {
            byte b = 0;
            try {
                b = (byte) in.read();
            }catch (SocketTimeoutException e){
                //读取超时，关闭当前socket
                Log.warri(" curr socket: " + socket + " read time out", String.valueOf(port));
                b = -1;
                Log.debug(" close the socket: " + socket, String.valueOf(port));
            }catch (SocketException e){
                //e.printStackTrace();
                //防止用户程序异常终止，没有关闭socket，客户端socket不会read阻塞，也不会返回-1，而是会抛出此Connection rest异常
                //当用户调用了shutdown强行关闭所有socket的时候，会发生该异常，记录日志
                Log.error(" curr socket: " + socket + " " + e.getMessage(), String.valueOf(port));
                //关闭当前异常socket
                b = -1;
                Log.debug(" close the socket: " + socket, String.valueOf(port));
            }
            if (b == -1) {
                //当前socket已经关闭
                return false;
            }
            if (b == '\n') {
                //第一次能读到，后面肯定能读到，此处不需要加异常处理，应该？ 目前没问题
                byte _r = (byte) in.read();
                byte _n = (byte) in.read();
                if (_r == '\r' && _n == '\n') {
                    return true;
                }
                out.write(b);
                out.write(_r);
                out.write(_n);
            } else {
                out.write(b);
            }
        }
    }

    public int getPort() {
        return this.port;
    }
}
