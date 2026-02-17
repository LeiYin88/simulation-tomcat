package org.example.mytomcat;

import java.nio.charset.Charset;

/**
 * <p>常量</p>
 * @author Yin
 * @since 2023/12/20 11:28 星期二
 * @version 1.1
 */
public class Constants {

    public static final String AUTHOR = "yin";

    public static final String VERSION = "1.1.0";

    //public static String BANNER_CLASS = "org.example.mytomcat.DefaultBanner";

    public static String BANNER_CLASS = "org.example.mytomcat.SimpleBanner";

    public static final Charset DEFAULT_ENCODING = Charset.forName("UTF-8");

    public static final String CONTENT_LENGTH = "Content-Length";

    public static final String CONTENT_TYPE = "Content-Type";

    public static final String APPLICATION_FORM_DEFAULT = "application/x-www-form-urlencoded";

    public static final String ACCEPT_RANGES = "Accept-Ranges";

    public static final String DATE = "Date";

    public static final String SERVER = "Server";

    public static final String MYTOMCAT_INDEX_PAGE_URL = "/";

    public static final String FAVICON_ICO_URL = "/favicon.ico";

    public static final String DEFAULT_SERVLET_URL_PATTERN = "/";

    public static final String DEFAULT_SERVLET_NAME = "defaultServlet";

    public static final String URL_HEADER = "url";

    public static final String METHOD_HEADER = "method";

    public static final String VERSION_HEADER = "version";

    public static final String SERVER_NAME = "Simulation-Tomcat";

    public static final String HTTP_HEADER_CONNECTION = "Connection";

    public static final String CONNECTION_STATE_CLOSE = "close";

    public static final String CONNECTION_STATE_KEEP_ALIVE = "Keep-Alive";

    public static final String ACCEPT_RANGES_BYTES = "bytes";

    //favicon.ico path 以tomcat_home为相对路径
    public static final String FAVICON_ICO_FILE_PATH = "webapps\\ROOT\\favicon.ico";

    public static Integer port;

    public static final String COMPUTER_NAME = System.getenv("COMPUTERNAME");

    public static final String ERROR = "ERROR";

    public static final String DEBUG = "DEBUG";

    public static final String INFOR = "INFOR";

    public static final String WARRI = "WARRI";

    public static final String  OK   = " O K ";

    public static final String STANDARD_DIR_CLASSES = "classes";

    public static final String STANDARD_DIR_WEB_INF = "WEB-INF";

    public static final String STANDARD_DIR_WABAPPS = "webapps";

    public static final String DEFAULT_HOST = "localhost";

    public static final int DEFAULT_SERVER_PORT = 8888;

    public static final int DEFAULT_TOMCAT_PORT = 8080;

    /*  以下值需要设置合理，如果有必要，则可以更换阻塞队列和拒绝策略
     *
     *   测试机配置：i5 10400 + 1650 + 16
     *   当前参数配置实测 : 核心线程 10 ，最大线程 100
     *                  10000并发 3s完成响应 异常为0 request to : localhost:8080
     *                  50000并发 16s完成响应 异常为50% request to : localhost:8080
     *
     *   当将核心线程调至 100 ，最大线程调至 1000 时， 50000并发 12s完成响应， 异常为0 request to : localhost:8080
     *   window 中一个进程允许的最大线程为2000， 且线程调大了也没有意义，可能还会导致响应变慢，因为维护线程也需要时间
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    //线程池 核心线程数
    public static int CORE_POOL_SIZE = 100;

    //线程池 最大线程数
    public static int MAX_IMUM_POOL_SIZE = 150;

    //默认使用无界队列
    //可能会导致发生OOM，造成不可预估的错误！
    public static boolean USEUNLIMITED = true;

    //其他线程的存活时间 s
    public static long KEEP_ALIVE_TIME = 5;

    //有界阻塞队列最大长度
    public static int BOLOCK_QUEUE_SIZE = 1000;

    //是否显示socket的关闭情况
    public static boolean LOG_SOCKET_CLOSE = true;

    //ms
    public static final int DEFAULT_READ_TIME_OUT = 30000;

    //is enable banner
    public static final boolean enableBanner = true;

    //线上环境:
    //public static final String TOMCAT_HOME = System.getenv("TOMCAT_HOME");

    //开发环境：
    public static final String TOMCAT_HOME = System.getProperty("user.dir");

    //test
    //public static final String TOMCAT_HOME = null;
}
