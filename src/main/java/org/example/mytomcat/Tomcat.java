package org.example.mytomcat;

import java.io.IOException;
import java.io.InputStream;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.net.*;

import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.example.mytomcat.Constants.*;

/**
 * <pre>
 *  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  *                                                                     *
 *  *                                                                     *
 *  *                                                                     *
 *  *                                                                     *
 *  *                          Bootstrap Tomcat!                          *
 *  *                                                                     *
 *  *                                                                     *
 *  *                                                                     *
 *  *                                                                     *
 *  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  </pre>
 * @author Yin
 * @since 2023/12/20 11:28 星期二
 * @version 1.1
 */
public class Tomcat {
    /**
     * 连接器
     */
    private Connector connector;
    /**
     * 管理所有的项目上下文
     */
    private Context context;
    /**
     * 连接器监听的端口
     */
    private int port;
    /**
     * 默认监听的主机
     */
    private static String host = DEFAULT_HOST;
    /**
     * tomcat server监听的端口号
     */
    private static int serverPort = DEFAULT_SERVER_PORT;
    /**
     * socket read超时时间
     */
    protected static int readTimeOut = DEFAULT_READ_TIME_OUT;
    /**
     * tomcat connector状态
     */
    protected volatile static boolean serverState;

    Logger logger = Logger.getLogger("tomcat");


    public Tomcat(int port) {
        this.port = port;
    }

    //start tomcat
    public synchronized static void main(String[] args) throws Exception {
        if (TOMCAT_HOME == null) {
            throw new Error("need environment variable: TOMCAT_HOME");
        }

        //如果要自定义banner，只需要设置banner.classname，value为Banner接口的实现类
        String clazz = System.getProperty("banner.classname");
        if (clazz != null && !"".equals(clazz.trim())){
            BANNER_CLASS = clazz;
        }

        if (enableBanner) {
            printBanner();
        }

        Log.infor(" TOMCAT_HOME=" + TOMCAT_HOME, args.length > 0 ? args[0] : String.valueOf(DEFAULT_TOMCAT_PORT));

        long start = System.currentTimeMillis();

        Log.infor(" simulation-tomcat start", args.length > 0 ? args[0] : String.valueOf(DEFAULT_TOMCAT_PORT));

        int port = DEFAULT_TOMCAT_PORT;
        try {
            port = Integer.parseInt(args[0]);
            Log.infor(" use custom port: " + port, String.valueOf(port));
        } catch (Exception e) {
            Log.infor(" use default port: " + port, String.valueOf(port));
        }
        if (args.length > 1 && args[1] != null && !"".equals(args[1].trim())){
            //如果有配置主机，那么就用该主机
            host = args[1];
            Log.infor(" bind host: " + host, String.valueOf(port));
        }else{
            Log.infor(" bind default host: " + host, String.valueOf(port));
        }
        Constants.port = port;
        Tomcat tomcat = new Tomcat(port);
        tomcat.start();
        long end = System.currentTimeMillis();
        Log.ok(" simulation-tomcat startup time : " + (end - start) + "ms", args.length > 0 ? args[0] : String.valueOf(DEFAULT_TOMCAT_PORT));
        Log.infor(" ---------------------------------------------", String.valueOf(DEFAULT_TOMCAT_PORT));
    }

    private static void printBanner() {
        try {
            Banner banner = (Banner) Class.forName(BANNER_CLASS).getConstructor().newInstance();
            banner.printBanner();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        logger.log(Level.INFO, "Inner Tomcat Container Connector Start");
        serverState = true;
        this.connector = new Connector(port);
        this.context = new StandardContext();
        startInternal();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.log(Level.INFO, "Tomcat Server Start");
        getServer(serverPort).start();
    }

    private void startInternal() {
        connector.start(context);
        context.start();
    }

    private Thread getServer(int serverPort) {
        //单独开启一个线程监听8888
        Thread t =  new Thread(() -> {
            ServerSocket serverSocket;
            try {
                serverSocket = new ServerSocket(serverPort);
                //存放命令的字节数组，不需要太大，目前只有一个命令，stop
                //既然没有规定命令传输的协议，那就规定大小，只接收4个字节的数据，那么以后拓展的命令就要为4个字节
                byte[] command = new byte[4];
                int i = 0;
                flag1:
                while (true) {
                    Log.debug(" listen to " + serverPort +" wait the tomcat server command", String.valueOf(port));
                    Socket socket = serverSocket.accept();
                    Log.debug(" connect socket: " + socket, String.valueOf(serverPort));
                    //设置读取的超时时间为10s，防止用户不关闭连接
                    socket.setSoTimeout(readTimeOut);
                    flag2:
                    while (true) {
                        InputStream in = socket.getInputStream();
                        int len = 0;
                        while (i != command.length) {
                            try {
                                len = in.read();
                            }catch (SocketTimeoutException | SocketException e){
                                //说明读取超时，关闭当前socket连接，重新建立连接
                                // 或
                                //防止用户程序异常终止，没有关闭socket，客户端socket不会read阻塞，也不会返回-1，而是会抛出此Connection rest异常
                                //关闭异常的socket
                                Log.error(" curr socket: " + socket + " read time out", String.valueOf(serverPort));
                                e.printStackTrace();
                                socket.close();
                                Log.debug(" close the socket: " + socket, String.valueOf(serverPort));
                                break flag2;
                            }
                            if (len == -1) {
                                break flag2;
                            }
                            command[i] = (byte) len;
                            i++;
                        }
                        String s = new String(command);
                        if ("stop".equalsIgnoreCase(s)) {
                            try {
                                connector.close();
                            }catch (IOException e){
                                Log.warri(" ignore curr shutdown because tomcat busy, wait minute try again", String.valueOf(serverPort));
                                //关闭此socket，重新监听8888
                                socket.close();
                                continue flag1;
                            }
                            Log.warri(" close curr socket: " + socket, String.valueOf(serverPort));
                            socket.close();
                            serverSocket.close();
                            break flag1;
                        }
                        socket.getOutputStream().write("the command does not exist, can used: stop".getBytes(StandardCharsets.UTF_8));
                        i = 0;
                    }
                }
                Log.infor(" close inner simulation-tomcat server " + serverPort, String.valueOf(serverPort));
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(500);
                System.out.println("bye!");
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
        t.setName("server-thread-0");
        return t;
    }
}
