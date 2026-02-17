package org.example.mytomcat;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.example.mytomcat.Constants.*;

/**
 * @author Yin
 * @since 2023/12/20 11:28 星期二
 * @version 1.1
 */
public class Log {
    private static Logger logger = Logger.getLogger("tomcat");

    private static String placeHolder = "\033[%dm%s\033[0m";

    private static String calMessage = logCal().toString();

    public static void error(String message, String... args) {
        info(ERROR, message, args);
    }

    public static void debug(String message, String... args) {
        info(DEBUG, message, args);
    }

    public static void infor(String message, String... args) {
        info(INFOR, message, args);
    }

    public static void warri(String message, String... args) {
        info(WARRI, message, args);
    }

    public static void ok(String message, String... args) {
        info(OK, message, args);
    }

    public static void info(String state, String message, String... args) {
        simpleLog(state, message);
        /*System.out.println(logZh(state) +
                           String.format(placeHolder, 32, LocalDateTime.now() + "[" + COMPUTER_NAME + "-" + DEFAULT_HOST + "-" + args[0] + "-" + Thread.currentThread().getName() + "] ") +
                           (state.equals(ERROR) ? String.format(placeHolder, 31,"[" + state + "]") : String.format(placeHolder, 36,"[" + state + "]")) +
                           message);*/
    }

    public static void simpleLog(String state, String message){
        switch (state){
            case INFOR:
            case OK: logger.log(Level.INFO, message);break;
            case DEBUG: logger.log(Level.CONFIG, message);break;
            case WARRI: logger.log(Level.WARNING, message);break;
            case ERROR: logger.log(Level.FINE, message);break;
        }

    }

    private static StringBuilder logCal() {
        StringBuilder sb = new StringBuilder();
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        switch (month) {
            case 1:
                sb.append("一月 ");
                break;
            case 2:
                sb.append("二月 ");
                break;
            case 3:
                sb.append("三月 ");
                break;
            case 4:
                sb.append("四月 ");
                break;
            case 5:
                sb.append("五月 ");
                break;
            case 6:
                sb.append("六月 ");
                break;
            case 7:
                sb.append("七月 ");
                break;
            case 8:
                sb.append("八月 ");
                break;
            case 9:
                sb.append("九月 ");
                break;
            case 10:
                sb.append("十月 ");
                break;
            case 11:
                sb.append("十一月 ");
                break;
            case 12:
                sb.append("十二月 ");
                break;
        }
        return sb;
    }

    private static String logZh(String state){
        String mes = null;
        switch (state){
            case INFOR:
            case OK:
            case DEBUG:mes = String.format(placeHolder, 32, calMessage.concat("信息 "));break;
            case WARRI:mes = String.format(placeHolder, 33, calMessage.concat("警告 "));break;
            case ERROR:mes = String.format(placeHolder, 31, calMessage.concat("严重 "));break;
        }
        return mes;
    }
}
