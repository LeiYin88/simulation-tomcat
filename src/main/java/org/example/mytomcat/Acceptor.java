package org.example.mytomcat;

import org.example.mytomcat.exception.NotFoundContextPathException;
import org.example.mytomcat.servlet.DefaultServlet;
import org.example.mytomcat.servlet.ServletContext;
import org.example.mytomcat.servlet.ServletWrapper;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.BindException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

import static org.example.mytomcat.Constants.*;
import static org.example.mytomcat.Tomcat.*;

/**
 * @author Yin
 * @version 1.1
 * @since 2023/12/20 11:28 星期二
 */
public class Acceptor extends Thread {
    private Connector connector;

    private Context context;

    private Map<File, ServletContext> servletContexts;

    private DefaultServlet defaultServlet;

    //维护了所有的任务，供关闭使用
    protected List<SocketWrapper> tasks = new LinkedList<>();

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(
            CORE_POOL_SIZE, MAX_IMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
            USEUNLIMITED ? new LinkedBlockingQueue<>() : new ArrayBlockingQueue<>(BOLOCK_QUEUE_SIZE),
            Executors.defaultThreadFactory());

    public ThreadPoolExecutor getExecutor() {
        return executor;
    }

    public void start(Connector connector) {
        this.connector = connector;
        startInternal();
    }

    private void startInternal() {
        this.start();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        synchronized (Tomcat.class) {
            Log.infor(" listen to the " + port + " wait a HTTP request", String.valueOf(port));
            while (serverState) {
                Socket socket = null;
                SocketWrapper socketWrapper = null;
                try {
                    //一个线程接收请求，分派给线程池处理
                    //fixme 不知道是否有问题
                    //fixme 可能还存在未解决的浏览器的 keep-alive问题
                    socket = connector.listen(connector.getPort());
                    socket.setSoTimeout(readTimeOut);
                    socketWrapper = new SocketWrapper(socket, this, connector);
                    tasks.add(socketWrapper);
                    executor.execute(socketWrapper);
                } catch (BindException e) {
                    e.printStackTrace();
                    System.exit(-1);
                } catch (RejectedExecutionException e) {
                    //线程池饱和，任务队列饱和，执行默认拒绝策略，即并发量超过队列大小
                    //处理线程池饱和的情况
                    Log.error(" " + e.getMessage(), String.valueOf(connector.getPort()));
                    e.printStackTrace();
                    try {
                        if (socket != null) {
                            try {
                                //关闭socket
                                socket.close();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                        if (socketWrapper != null) {
                            tasks.remove(socketWrapper);
                            socketWrapper = null;
                        }
                        //睡眠5s，停止接收socket，留时间给其他线程处理完成
                        sleep(5000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                } catch (Exception e) {
                    Log.error(" " + e.getMessage(), String.valueOf(connector.getPort()));
                    e.printStackTrace();
                    //nothing
                }
            }
        }
    }

    public void close() {
        Log.warri(" prepare close simulation-tomcat server", String.valueOf(connector.getPort()));
        if (servletContexts != null && !servletContexts.isEmpty()) {
            destroy0();
        }
    }

    private void destroy0() {
        Log.infor(" prepare destroy all servlet", String.valueOf(connector.getPort()));
        defaultServlet.destroy();
        servletContexts.forEach((file, servletContext) -> {
            List<ServletWrapper> wrappers = servletContext.getWrappers();
            wrappers.forEach(wrapper -> {
                if (wrapper != null && wrapper.getServlet() != null)
                    wrapper.getServlet().destroy();
            });
        });
    }

    protected void finishRequest(Map<HttpRequest, HttpResponse> currHttp, Socket socket, boolean isClose) throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Set<HttpRequest> http = currHttp.keySet();
        HttpRequest httpRequest = http.iterator().next();
        HttpResponse httpResponse = currHttp.get(httpRequest);

        if (isClose) {
            httpResponse.setHeader(HTTP_HEADER_CONNECTION, CONNECTION_STATE_CLOSE);
        }

        String url = httpRequest.getHeader(URL_HEADER);
        if (MYTOMCAT_INDEX_PAGE_URL.equals(url)) {
            writeServerInfo(socket, isClose ? CONNECTION_STATE_CLOSE : CONNECTION_STATE_KEEP_ALIVE);
        } else if (FAVICON_ICO_URL.equals(url)) {
            writeFavicon(socket, isClose ? CONNECTION_STATE_CLOSE : CONNECTION_STATE_KEEP_ALIVE);
        } else {
            if (this.servletContexts == null) {
                servletContexts = context.getServletContexts();
            }
            if (!servletContexts.isEmpty()) {
                //判断条件为是否有存在的servlet或资源，如果有则返回
                //实例化servlet
                //首先实例化DefaultServlet
                ServletWrapper defaultServletWrapper = context.getDefaultServletWrapper();
                defaultServlet = (DefaultServlet) defaultServletWrapper.getServlet();
                if (defaultServlet == null) {
                    //保证只实例化一次
                    defaultServlet = (DefaultServlet) defaultServletWrapper.getClazz().getConstructor().newInstance();
                    //调用init方法
                    defaultServlet.init(defaultServletWrapper.getConfig());
                    //默认的servlet管理所有的项目上下文
                    defaultServlet.setServletContexts(servletContexts);
                    defaultServletWrapper.setServlet(defaultServlet);
                }
                String urlPattern = defaultServletWrapper.getUrlPattern();
                try {
                    if (DEFAULT_SERVLET_URL_PATTERN.equals(urlPattern)) {
                        //使用defaultServlet处理所有其他请求
                        //此后所有逻辑由默认servlet完成
                        defaultServlet.service(httpRequest, httpResponse);
                        //完成响应
                        finishResponse(socket, httpResponse);
                        return;
                    }
                } catch (NotFoundContextPathException e) {
                    //do nothing
                } catch (Exception e) {
                    Log.error(" " + e.getMessage(), String.valueOf(connector.getPort()));
                    e.printStackTrace();
                    writeErrorPage(e, socket, isClose ? CONNECTION_STATE_CLOSE : CONNECTION_STATE_KEEP_ALIVE);
                    return;
                }
                //如果走到此处，则说明没有配置默认的servlet处理静态资源，继续往下走返回404页面
            }
            //写入404页面
            write404(url, socket, isClose ? CONNECTION_STATE_CLOSE : CONNECTION_STATE_KEEP_ALIVE);
        }
    }

    private void writeFavicon(Socket socket, String connection) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String tomcatHome = Constants.TOMCAT_HOME;
        FileInputStream in = new FileInputStream(tomcatHome + "\\" + FAVICON_ICO_FILE_PATH);
        int len;
        while ((len = in.read()) != -1) {
            out.write(len);
        }
        in.close();
        //写入响应头 写入数据
        finishResponse(socket, out, connection);
    }

    private void writeServerInfo(Socket socket, String connection) throws IOException {
        //todo 此处写死了
        byte[] responseMessage = ("HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html\r\n" +
                "Connection: " + connection + "\r\n" +
                "Content-Length: 115\r\n" +
                "Date: " + LocalDateTime.now() + "\r\n" +
                "Server: " + SERVER_NAME + "\r\n" +
                "\r\n" +
                "<html><body><h1 style=\"color: darkturquoise\">Simulation-Tomcat</h1><hr/>author: Yin<br>version: " + VERSION + "</body></html>")
                .getBytes(DEFAULT_ENCODING);
        socket.getOutputStream().write(responseMessage);
    }

    private void writeErrorPage(Exception e, Socket socket, String connection) throws IOException {
        //todo 此处写死了
        ByteArrayOutputStream error = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(error));

        byte[] errorMessage = error.toByteArray();
        byte[] errorBody = new String(errorMessage).replace("\n", "<br/>").getBytes(StandardCharsets.UTF_8);
        String htmlBody = "<h1 style=\"color: deeppink\">ERROR!</h1><hr/><p>exception message:</p>";

        //其他异常，响应错误页
        byte[] responseHeaderAndPartBody = ("HTTP/1.1 500 Internal Server Error\r\n" +
                "Date: " + LocalDateTime.now() + "\r\n" +
                "Content-Length: " + (errorBody.length + 69) + "\r\n" +
                "Content-Type: text/html\r\n" +
                "Server: " + SERVER_NAME + "\r\n" +
                "Connection: " + connection + "\r\n" +
                "\r\n" +
                htmlBody).getBytes(StandardCharsets.UTF_8);

        socket.getOutputStream().write(responseHeaderAndPartBody);
        socket.getOutputStream().write(errorBody);
    }

    private void write404(String url, Socket socket, String connection) throws IOException {
        //todo 此处写死了
        byte[] responseBody = ("<!doctype html><html><head><title>404 Not Found</title></head><body><h1 style=\"color: orangered;\">Not Found</h1><h4 style=\"color: yellowgreen\">The requested URL " + url + " was not found on this server.</h4><hr> <address><label style=\"color: darkturquoise\">Simulation-Tomcat</label> at <label style=\"color: red\">" + DEFAULT_HOST + "</label> Port <label style=\"color: red\">" + port + "</label></address></body></html>")
                .getBytes(StandardCharsets.UTF_8);
        byte[] responseHeader = ("HTTP/1.1 404 NOT FOUND\r\n" +
                "Date: " + LocalDateTime.now() + "\r\n" +
                "Content-Length: " + responseBody.length + "\r\n" +
                "Content-Type: text/html\r\n" +
                "Server: " + SERVER_NAME + "\r\n" +
                "Connection: " + connection + "\r\n" +
                "\r\n").getBytes(StandardCharsets.UTF_8);
        socket.getOutputStream().write(responseHeader);
        socket.getOutputStream().write(responseBody);
    }

    //return static resource or servlet response
    private void finishResponse(Socket socket, HttpResponse httpResponse) throws IOException {
        //内容协商交由对应的servlet，此处写入通用的头部
        Map<String, String> header = httpResponse.getHeader();

        /*
            全部设置为短连接，不支持'''浏览器'''的长连接

            每一次请求都要设置该响应头，告诉浏览器不要保持长连接，否则会出问题
            因为浏览器不会只在一个socket上复用，可以能会开启多个socket，
            导致服务端依旧在原来的socket上读数据，而客户端在原先的socket上传输了完了数据，开启了新的socket，导致双方都阻塞
            因为该服务器的逻辑是只有一个socket连接，如果客户端开启了新的socket连接，那么需要等待当前socket关闭
            才能成功建立连接。

            本质问题是：浏览器希望开启多个socket复用
                      该服务器希望开启一个socket在此socket上复用
            二者的差异导致该服务端不能让浏览器复用，故设置该字段标志为短链接
         */
        //更新后的代码支持复用
        String r0 = header.get(HTTP_HEADER_CONNECTION);
        String r1 = header.get(ACCEPT_RANGES);
        String r2 = header.get(DATE);
        String r3 = header.get(SERVER);

        //用户没有设置则设置，有则使用用户的
        if (isBlank(r0)) {
            header.put(HTTP_HEADER_CONNECTION, CONNECTION_STATE_KEEP_ALIVE);
        } else if (isBlank(r1)) {
            header.put(ACCEPT_RANGES, ACCEPT_RANGES_BYTES);
        } else if (isBlank(r2)) {
            header.put(DATE, LocalDateTime.now().toString());
        } else if (isBlank(r3)) {
            header.put(SERVER, SERVER_NAME);
        }

        ByteArrayOutputStream out = httpResponse.getBody();
        byte[] binary = out.toByteArray();
        int length = binary.length;
        if (length == 0) {
            //write zero
            header.put(Constants.CONTENT_LENGTH, String.valueOf(0));
        } else {
            //写入对应长度
            header.put(Constants.CONTENT_LENGTH, String.valueOf(length));
        }
        writeToSocket(socket, httpResponse);
    }

    public boolean isBlank(String str) {
        return str == null || "".equals(str);
    }

    private void writeToSocket(Socket socket, HttpResponse httpResponse) throws IOException {
        ByteArrayOutputStream responseMessage = new ByteArrayOutputStream();
        writeResponseLine(responseMessage);
        httpResponse.getHeader().forEach((k, v) -> {
            try {
                responseMessage.write((k + ": " + v + "\r\n").getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                Log.error(e.getMessage(), String.valueOf(connector.getPort()));
            }
        });
        responseMessage.write('\r');
        responseMessage.write('\n');
        responseMessage.write(httpResponse.getBody().toByteArray());
        socket.getOutputStream().write(responseMessage.toByteArray());
    }

    private void writeResponseLine(ByteArrayOutputStream responseMessage) throws IOException {
        responseMessage.write("HTTP/1.1 200 OK\r\n".getBytes(StandardCharsets.UTF_8));
    }

    //return favicon.ico
    private void finishResponse(Socket socket, ByteArrayOutputStream out, String connection) throws IOException {
        byte[] binary = out.toByteArray();
        String length = String.valueOf(binary.length);
        //todo 此处写死了
        String responseHeader = "HTTP/1.1 200 OK\r\n" +
                "Accept-Ranges: bytes\r\n" +
                "Content-Length: " + length + "\r\n" +
                "Date: " + LocalDateTime.now() + "\r\n" +
                "Server: " + SERVER_NAME + "\r\n" +
                "Connection: " + connection + "\r\n" +
                "Content-Type: image/x-icon\r\n\r\n";
        byte[] binaryHeader = responseHeader.getBytes(StandardCharsets.UTF_8);
        byte[] responseMessage = new byte[binaryHeader.length + binary.length];
        System.arraycopy(binaryHeader, 0, responseMessage, 0, binaryHeader.length);
        System.arraycopy(binary, 0, responseMessage, binaryHeader.length, binary.length);
        socket.getOutputStream().write(responseMessage);
    }
}
