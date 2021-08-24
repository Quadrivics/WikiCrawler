package com.sscb;

public class Main {
    private static DataBuilder dataBuilder = new DataBuilder(3, 3) ;
    private static RecursionTester rt = new RecursionTester();

    public static void main(String[] args) {
//        dataBuilder.runMainCrawl("Open-source intelligence");
        Object[] intArray = {1,2,3,
                new Object[]{5, 6}};
        System.out.println(rt.recurse(intArray, 0,3));
    }

}
