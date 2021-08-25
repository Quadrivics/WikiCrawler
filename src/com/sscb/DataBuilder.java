package com.sscb;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class DataBuilder {
    final static String baseUrl = "https://en.wikipedia.org/api/rest_v1/page/html";
    private Helpers helper = new Helpers();
    private int maxLeadLinkAmount;
    private int maxDepth;
    static int depth = 0;
    static int leadLinkAmount = 0;
    private String[][] pageLinkArray;
    private Page[] pageArray;
    private List<String> visitedLinks = new ArrayList<>();
    private Page page;
    private TraversePages tp = new TraversePages(this);

    DataBuilder(int maxLeadLinkAmount, int maxDepth) {
        this.maxLeadLinkAmount = maxLeadLinkAmount;
        this.maxDepth = maxDepth;
        this.pageLinkArray = new String[maxLeadLinkAmount][2];
    }

    void startCrawl(Object subject) {
        if (subject instanceof String) {
            startCrawl(crawlPage((Page) tp.recurse(subject, depth, maxDepth)));
        } else if (subject instanceof Object[]) {
            depth++;
            for(Page page : (Page[]) subject) {
                startCrawl(crawlPage((Page) tp.recurse(page, depth, maxDepth)));
            }
        } else if (subject instanceof Page) {
            if (!visitedLinks.contains(((Page) subject).getShortUrl())) {
                startCrawl(crawlPage((Page) subject));
            }
        }
    }

    Object[] crawlPage(Page page) {
        pageLinkArray = helper.getLimitedSortedScoreArray(page.getSortedLeadLinkArray(), maxLeadLinkAmount);
        Object[] tempArray = new Page[maxLeadLinkAmount];
        if (leadLinkAmount < maxLeadLinkAmount) {
            for (int i = 0; i < pageLinkArray.length; i++) {
                tempArray[i] = crawlPage(pageLinkArray[i][0]);
            }
            leadLinkAmount++;
        }
        return tempArray;
    }

    Page crawlPage(String pageName) {
        String shortUrl = helper.buildShortLink(pageName);
        String url = baseUrl + shortUrl;
        Page page = null;
        long startTime = System.nanoTime();
        try {
            Document doc = Jsoup.connect(url).get();
            page = new Page(doc, shortUrl);
            System.out.println("Gathering info for searchterm: '" + page.getTitle().toLowerCase() + "' with url " + url);
            visitedLinks.add(shortUrl);
            printPageInfo(page);
        } catch (IOException ioex) {
            System.out.println("Could not analyze page for url: " + url);
            System.out.println(ioex.toString());
        }

        System.out.println("Duration " + (System.nanoTime() - startTime) / 1000000 + " miliseconds");
        System.out.println("---------------------------------------------------");
        return page;
    }


    private void printPageInfo(Page page) {
        String indent = buildIndent(depth);
//        System.out.println("Top " + page.getLeadLinkAmount() + " most used words: ");
//        helper.printLimitedSortedScoreArray(page.getSortedWordScoreArray(), 3);

        System.out.println("Prioritized leadlinks are: ");
        for (int i = 0; i < maxLeadLinkAmount; i++) {
            System.out.println(indent + (i + 1) + ") " + page.getSortedLeadLinkArray()[i][0].toLowerCase());
        }
//        System.out.println("Words/Unique: " + page.getWordArray().length + " / " + page.getUniqueWordList().size());
//        System.out.println("Links/Wiki's: " + page.getPageLinkList().size() + " / " + page.getLeadLinkList().size());
    }

    String buildIndent(int amount) {
        String space = " ";
        String spaces = space;
        if (amount > 0) {
            for (int i = 0; i < amount; i++) {
                spaces = space.concat(space);
            }
            return spaces;
        } else {
            return space;
        }
    }


}
