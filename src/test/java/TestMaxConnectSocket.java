import java.io.IOException;
import java.net.Socket;

public class TestMaxConnectSocket {
    public static void main(String[] args) throws IOException {
        //客户端无法创建那么多的socket连接
        //因为端口好最多为65535，但也不意为着就能创建65535个socket
        for (int i = 0; i < 100000; i++) {
            Socket socket = new Socket("localhost", 8080);
        }
    }
}
