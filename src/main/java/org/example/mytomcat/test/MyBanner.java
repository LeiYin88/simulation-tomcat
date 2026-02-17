package org.example.mytomcat.test;

import org.example.mytomcat.Banner;

public class MyBanner implements Banner {
    @Override
    public void printBanner() {
        System.out.println("asdklfjalks;djfklajs;dlkf" +
                "asdkfjlaskl;dfj;klasd" +
                "asdfjk;alskdfj;lasd");
    }

    public static void main(String[] args) {
        System.out.println(String.format("\033[%dm%s\033[0m",36,"字体"));
    }
}
