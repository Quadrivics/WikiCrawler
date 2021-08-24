//package com.sscb;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//class DataBuilderBackup {
//    final static String baseUrl = "https://en.wikipedia.org/api/rest_v1/page/html/";
//    private Helpers helper = new Helpers();
//    private int maxLeadLinkAmount;
//    private int maxClickTroughAmount;
//    static int clickTroughAmount = 0;
//    static int leadLinkAmount = 0;
//    private String[][] pageLinkArray;
//    private List<String> visitedLinks = new ArrayList<>();
//
//    DataBuilderBackup(int maxLeadLinkAmount, int maxClickTroughAmount) {
//        this.maxLeadLinkAmount = maxLeadLinkAmount;
//        this.maxClickTroughAmount = maxClickTroughAmount;
//        this.pageLinkArray = new String[maxLeadLinkAmount][2];
//    }
//
//    void runMainCrawl(String subject) {
//        Page leadPage = crawlPage(subject.toLowerCase());
//        pageLinkArray = helper.getLimitedSortedScoreArray(leadPage.getSortedLeadLinkArray(), maxLeadLinkAmount);
//        if (leadLinkAmount < maxClickTroughAmount) {
//            for (String[] linkArray : pageLinkArray) {
//                crawlPage(linkArray[0]);
//            }
//            leadLinkAmount++;
//        }
//    }
//
//    Page crawlPage(String pageName) {
//        String shortUrl = helper.buildShortLink(pageName);
//        String url = baseUrl + shortUrl;
//        Page page = null;
//        long startTime = System.nanoTime();
//        if (!url.contains(pageName)) {
//            try {
//                Document doc = Jsoup.connect(url).get();
//                page = new Page(doc, url);
//                System.out.println("Gathering info for searchterm: '" + page.getTitle().toLowerCase() + "' with url " + url);
//                visitedLinks.add("./" + shortUrl);
//                printPageInfo(page);
//            } catch (IOException ioex) {
//                System.out.println("Could not analyze page for url: " + url);
//                System.out.println(ioex.toString());
//            }
//            System.out.println("Duration " + (System.nanoTime() - startTime) / 1000000 + " miliseconds");
//            System.out.println("--------------------------");
//        }
//        return page;
//    }
//
//
//    private void printPageInfo(Page page) {
////        System.out.println("Top " + page.getLeadLinkAmount() + " most used words: ");
////        helper.printLimitedSortedScoreArray(page.getSortedWordScoreArray(), 3);
//        System.out.println("Prioritized leadlinks are: ");
//        helper.printLimitedSortedScoreArray(page.getSortedLeadLinkArray(), maxLeadLinkAmount);
////        System.out.println("Words/Unique: " + page.getWordArray().length + " / " + page.getUniqueWordList().size());
////        System.out.println("Links/Wiki's: " + page.getPageLinkList().size() + " / " + page.getLeadLinkList().size());
//    }
//
//
//}
