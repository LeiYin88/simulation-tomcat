package org.example.mytomcat;

/**
 * @author Yin
 * @since 2023/12/20 11:28 星期二
 * @version 1.1
 */
public class DefaultBanner implements Banner {

    String placeholder = "\033[%dm%s\033[0m";

    @Override
    public void printBanner() {
        System.out.println(String.format(placeholder, 33, "    _ooo--.") + "                       ________  ___  _____ ______   ___  ___  ___       ________  _________  ___  ________  ________      \n" +
                String.format(placeholder, 33, "     @@@=@MMM\\.`,_.',-") + "           |\\   ____\\|\\  \\|\\   _ \\  _   \\|\\  \\|\\  \\|\\  \\     |\\   __  \\|\\___   ___\\\\  \\|\\   __  \\|\\   ___  \\    \n" +
                String.format(placeholder, 33, "   _.\\X/\"/\"   \\  33,") + "             \\ \\  \\___|\\ \\  \\ \\  \\\\\\__\\ \\  \\ \\  \\\\\\  \\ \\  \\    \\ \\  \\|\\  \\|___ \\  \\_\\ \\  \\ \\  \\|\\  \\ \\  \\\\ \\  \\   \n" +
                String.format(placeholder, 33, "  ===A   |     \\ P\"\"B") + "             \\ \\_____  \\ \\  \\ \\  \\\\|__| \\  \\ \\  \\\\\\  \\ \\  \\    \\ \\   __  \\   \\ \\  \\ \\ \\  \\ \\  \\\\\\  \\ \\  \\\\ \\  \\  \n" +
                String.format(placeholder, 33, "    /@,_ (  __,/\"\"\\.M\\") + "             \\|____|\\  \\ \\  \\ \\  \\    \\ \\  \\ \\  \\\\\\  \\ \\  \\____\\ \\  \\ \\  \\   \\ \\  \\ \\ \\  \\ \\  \\\\\\  \\ \\  \\\\ \\  \\ \n" +
                String.format(placeholder, 33, "    |; \\\"/\\\"_,/ / .'.A") + "               ____\\_\\  \\ \\__\\ \\__\\    \\ \\__\\ \\_______\\ \\_______\\ \\__\\ \\__\\   \\ \\__\\ \\ \\__\\ \\_______\\ \\__\\\\ \\__\\\n" +
                String.format(placeholder, 33, "    \\,\\._><-__./    \"V") + "              |\\_________\\|__|\\|__|     \\|__|\\|_______|\\|_______|\\|__|\\|__|    \\|__|  \\|__|\\|_______|\\|__| \\|__|\n" +
                String.format(placeholder, 33, "     \\F _       a_3R\"---,.") + "           \\|_________|                                                                                      \n" +
                String.format(placeholder, 33, "      _>\"#           _   )") + "\n" +
                String.format(placeholder, 33, "     (  /           .@J  /") + "                                            " + String.format("\033[%dm%s\033[0m", 36, "Simulation Tomcat 1.0.1\n") +
                String.format(placeholder, 33, "     ) /           /    )") + "                                             " + String.format(placeholder, 36, "::author-yin") + "\n" +
                String.format(placeholder, 33, "     ( |           \\    /,") + "\n" +
                String.format(placeholder, 33, "     | \\     。      `,._,/ ___") + "             _________  ________  _____ ______   ________  ________  _________   \n" +
                String.format(placeholder, 33, "      \"=\\,          ]@7,.n| P @\\") + "          |\\___   ___\\\\   __  \\|\\   _ \\  _   \\|\\   ____\\|\\   __  \\|\\___   ___\\ \n" +
                String.format(placeholder, 33, "          7-______.  \\____.,   .)") + "         \\|___ \\  \\_\\ \\  \\|\\  \\ \\  \\\\\\__\\ \\  \\ \\  \\___|\\ \\  \\|\\  \\|___ \\  \\_| \n" +
                String.format(placeholder, 33, "          /  /     \\ \\      \\WWW/") + "              \\ \\  \\ \\ \\  \\\\\\  \\ \\  \\\\|__| \\  \\ \\  \\    \\ \\   __  \\   \\ \\  \\  \n" +
                String.format(placeholder, 33, "          |  |     |  |      \"\"'") + "                \\ \\  \\ \\ \\  \\\\\\  \\ \\  \\    \\ \\  \\ \\  \\____\\ \\  \\ \\  \\   \\ \\  \\ \n" +
                String.format(placeholder, 33, "   ___   /   \\.   ,/  \\._") + "                        \\ \\__\\ \\ \\_______\\ \\__\\    \\ \\__\\ \\_______\\ \\__\\ \\__\\   \\ \\__\\\n" +
                String.format(placeholder, 33, "/\"   \"\"\"      \\ (       \"\"\"\"\\") + "                     \\|__|  \\|_______|\\|__|     \\|__|\\|_______|\\|__|\\|__|    \\|__|\n" +
                String.format(placeholder, 33, "|(       ___.-'  \"--.       \\)") + "                  \n" +
                String.format(placeholder, 33, " \"\"-`\"\"\"\"            `--(__)/") + "                                    welcome to use the webServer");
    }
}
