package org.example.mytomcat;

import static org.example.mytomcat.Constants.*;

/**
 * @author Yin
 * @since 2023/12/20 11:28 星期二
 * @version 1.1
 */
public class SimpleBanner implements Banner{
    @Override
    public void printBanner() {
        System.out.println(" _________  ________  _____ ______   ________  ________  _________   \n" +
                "|\\___   ___\\\\   __  \\|\\   _ \\  _   \\|\\   ____\\|\\   __  \\|\\___   ___\\ \n" +
                "\\|___ \\  \\_\\ \\  \\|\\  \\ \\  \\\\\\__\\ \\  \\ \\  \\___|\\ \\  \\|\\  \\|___ \\  \\_| \n" +
                "     \\ \\  \\ \\ \\  \\\\\\  \\ \\  \\\\|__| \\  \\ \\  \\    \\ \\   __  \\   \\ \\  \\  \n" +
                "      \\ \\  \\ \\ \\  \\\\\\  \\ \\  \\    \\ \\  \\ \\  \\____\\ \\  \\ \\  \\   \\ \\  \\ \n" +
                "       \\ \\__\\ \\ \\_______\\ \\__\\    \\ \\__\\ \\_______\\ \\__\\ \\__\\   \\ \\__\\\n" +
                "        \\|__|  \\|_______|\\|__|     \\|__|\\|_______|\\|__|\\|__|    \\|__|\n" +
                "      welcome to use the webServer       " +    String.format("\033[%dm%s\033[0m", 36, SERVER_NAME +  " " + VERSION + "\n") +
                String.format("\033[%dm%s\033[0m", 36, "======================================>  ::author-" + AUTHOR + "\n")

        );
    }

    //test
    public static void main(String[] args) {
        new SimpleBanner().printBanner();
    }
}
