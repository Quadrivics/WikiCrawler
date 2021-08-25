package com.sscb;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class Page {
    private Helpers helper = new Helpers();
    private Document pageDocument;
    private String title;
    private String body;
    private String shortUrl;
    private String[] wordArray;
    private List<String> uniqueWordList;
    private HashMap<String, Integer> wordScoreMap;
    private String[][] sortedWordScoreArray;
    private List<String[]> pageLinkList = new ArrayList<>();
    private List<String[]> leadLinkList = new ArrayList<>();
    private List<String[]> uniqueLeadLinkList;
    private String[][] sortedLeadLinkArray;

    Page() {
    }

    Page(Document doc, String shortUrl) {
        this.pageDocument = doc;
        this.title = doc.title().toLowerCase();
        this.body = doc.text().toLowerCase();
        this.shortUrl = shortUrl;
        this.wordArray = helper.buildPageWordList(this);
        this.uniqueWordList = helper.buildUniqueWordList(wordArray);
        this.wordScoreMap = helper.buildWordScoreMap(uniqueWordList, wordArray, title);
        this.sortedWordScoreArray = helper.buildSortedWordScoreArray(wordScoreMap);
        helper.buildPageLinkLists(this);
        this.leadLinkList = helper.buildPageLeadLinkList(leadLinkList);
        this.uniqueLeadLinkList = helper.buildUniquePageLeadLinkList(leadLinkList);
        this.sortedLeadLinkArray = helper.buildSortedLeadLinkArray(uniqueLeadLinkList, sortedWordScoreArray);
    }

    Document getPageDocument() {
        return pageDocument;
    }

    String getTitle() {
        return title;
    }

    String getBody() {
        return body;
    }

    String getShortUrl() {
        return shortUrl;
    }

    String[] getWordArray() {
        return wordArray;
    }

    List<String> getUniqueWordList() {
        return uniqueWordList;
    }

    List<String[]> getPageLinkList() {
        return pageLinkList;
    }

    List<String[]> getLeadLinkList() {
        return leadLinkList;
    }

    public String[][] getSortedLeadLinkArray() {
        return sortedLeadLinkArray;
    }

}

