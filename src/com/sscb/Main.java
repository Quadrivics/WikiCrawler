package com.sscb;

public class Main {
    private static DataBuilder db = new DataBuilder(3, 3);

    public static void main(String[] args) {
        db.startCrawl("Open-source intelligence");
    }
}
