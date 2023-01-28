package me.mourjo;

public class Main {
    public static void main(String[] args) {
        System.out.println(String.join("\n", SessionInfo.getInfo("peter")));
        System.out.println(String.join("\n", SessionInfo.getInfo("janet")));
        System.out.println(String.join("\n", SessionInfo.getInfo("pam")));
        System.out.println(String.join("\n", SessionInfo.getInfo("barbara")));
        System.out.println(String.join("\n", SessionInfo.getInfo("jack")));
        System.out.println(String.join("\n", SessionInfo.getInfo("collin")));
        System.out.println(String.join("\n", SessionInfo.getInfo("george")));
    }
}