package org.example.mytomcat;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

import static org.example.mytomcat.Constants.*;

/**
 * @author Yin
 * @since 2023/12/20 11:28 星期二
 * @version 1.1
 */
public class SocketWrapper implements Runnable{
    private Socket socket;
    private Acceptor acceptor;
    private Connector connector;

    // true => Connection: close | false => Connection: keep-alive
    private boolean isClose = false;

    public SocketWrapper(Socket socket, Acceptor acceptor, Connector connector){
        this.socket = socket;
        this.acceptor = acceptor;
        this.connector = connector;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Map<HttpRequest, HttpResponse> currHttp = connector.getMessage(socket);
                if (socket.isClosed()) {
                    //表示对方关闭连接，跳过当前响应操作
                    /* log point
                    *  解开注释即可查看到socket的关闭情况，以及浏览器一次请求使用了多少个socket
                    *  当时打开如果请求过多而且中途又发生了异常，那么在idea控制台中会将异常信息盖下去
                    *  默认打开
                    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
                    if (LOG_SOCKET_CLOSE) {
                        Log.warri(" curr socket " + socket + " is close", String.valueOf(connector.getPort()));
                    }
                    return;
                }
                //如果当前正在执行任务的线程大于最大线程的一半
                /*
                 * 为什么要这样?
                 * 当线程池中执行的线程超过自己的一半时，我们就认为此时有较多用户来请求
                 * 如果之前的用户一直在原来的socket上发送请求，那么socket就不会释放，线程池会一直执行socketWrapper
                 * 那么后来的用户就可能进去到等待队列中等待，或者直接被拒绝，那么对于后来的用户不友好，此时我们则通知浏览器close掉socket
                 * 此时，线程池中的任务将会减少，其他用户则能争抢访问
                 */
                if (acceptor.getExecutor().getActiveCount() > (MAX_IMUM_POOL_SIZE / 2)){
                    isClose = true;
                }
                acceptor.finishRequest(currHttp, socket, isClose);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
        if (!socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
