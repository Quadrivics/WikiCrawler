package com.sscb;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

//TODO buildLimitedSortedLeadLinkList - wikicall doen of pagina bestaat, anders volgende uit de top zoveel searchwords pakken als leadlink
//TODO wordlist checkIfFilterWord refinen
//TODO bij scoring ook de bronvermelding gebruiken
//TODO treemap gebruiken voor scoren van woorden (https://www.youtube.com/watch?v=m7s6ulOJOAM)

class Helpers {

    // - Bouwen van lijsten en maps
    // bouwt woordenlijst voor wikipagina uit pagina body
    String[] buildPageWordList(Page page) {
        // vervangt alles wat niet a-z/A-Z is met niks en split tekst naar kleine letter arrayentries
        return page.getBody().replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
    }

    // bouwt unieke woordenlijst voor wikipagina vanuit pagina woordenarray
    List<String> buildUniqueWordList(String[] list) {
        List<String> uniqueWordList = new ArrayList<>();
        for (String word : list) {
            if (!uniqueWordList.contains(word.toLowerCase())) {
                uniqueWordList.add(word);
            }
        }
        return uniqueWordList;
    }

    // Scoren van woorden in de tekst
    HashMap<String, Integer> buildWordScoreMap(List<String> uniqueWordList, String[] wordArray, String pageName) {
        HashMap<String, Integer> tempMap = new HashMap<>();
        int wordUsedAmount;
        String[] scoreArray = new String[2];
        for (String uniqueWord : uniqueWordList) {
            wordUsedAmount = 0;
            for (String word : wordArray) {
                if (word.equalsIgnoreCase(uniqueWord)) {
                    wordUsedAmount++;
                    scoreArray[0] = word.toLowerCase();
                    scoreArray[1] = String.valueOf(wordUsedAmount);
                }
            }
            if (!checkIfFilterWord(uniqueWord, pageName) && wordUsedAmount > 4) {
                tempMap.put(scoreArray[0], Integer.valueOf(scoreArray[1]));
            }
        }
        return tempMap;
    }

    // bouwt paginalinlijst uit paginabody
    void buildPageLinkLists(Page page) {
        Elements docLinks = page.getPageDocument().select("a[href]");
        // voor elke link een List entry maken met een array van titel en text
        for (Element link : docLinks) {
            makeLinkArray(link.attr("href"), link.text(), page);
        }
    }


    List<String[]> buildPageLeadLinkList(List<String[]> linkList) {
        List<String[]> leadLinkList = new ArrayList<>();
        for (String[] array : linkList) {
            if (array[0].contains("./") && !array[0].contains("#")) {
                leadLinkList.add(new String[]{array[0].toLowerCase(), array[1].toLowerCase()});
            }
        }
        return leadLinkList;
    }

    // bouwt unieke wiki linkslijst vanuit ingevoerde lijst
    List<String[]> buildUniquePageLeadLinkList(List<String[]> list) {
        List<String[]> uniqueLeadLinkList = new ArrayList<>();
        LinkedHashSet<String> set = new LinkedHashSet<>();
        for (String[] arrayList : list) {
            if(set.add(arrayList[0])) {
                uniqueLeadLinkList.add(arrayList);
            }
        }
        return uniqueLeadLinkList;
    }

    String[][] buildSortedLeadLinkArray(List<String[]> uniqueLeadLinkList, String[][] sortedScoreArray) {
        List<String[]> tempLinkList = new ArrayList<>();
        int i = 0;
        for (String[] scoreArray : sortedScoreArray) {
            for (String[] linkArray : uniqueLeadLinkList) {
                if (linkArray[1].equalsIgnoreCase(scoreArray[0])) {
                    tempLinkList.add(linkArray);
                    i++;
                }
            }
        }
        String[][] tempLinkArray = new String[tempLinkList.size()][2];
        tempLinkArray = tempLinkList.toArray(tempLinkArray);
        return tempLinkArray;
    }

    // Collectiemutaties //
    // sorteert wordScoreMap van wikipage
    String[][] buildSortedWordScoreArray(HashMap<String, Integer> hashMap) {
        String[][] array = new String[hashMap.size()][2];
        List<Map.Entry<String, Integer>> scoredWordsList =
                new LinkedList<Map.Entry<String, Integer>>(hashMap.entrySet());
        Collections.sort(
                scoredWordsList,
                (i1,
                 i2) -> i2.getValue().compareTo(i1.getValue()));
        int i = 0;
        for (Map.Entry<String, Integer> mapEntry : scoredWordsList) {
            array[i][0] = mapEntry.getKey();
            array[i][1] = String.valueOf(mapEntry.getValue());
            i++;
        }
        return array;
    }

    // put array of link and text into list of a page
    // checked inputwoord op vergelijking met filterwoordenlijst
    private boolean checkIfFilterWord(String wordToCheck, String pageName) {
        String[] filterWords = {"open", "source", "osint", "pmid", "isbn", "it", "has", "or", "be", "am", "is", "are", "was", "were", "being", "been", "and", "this", "that", "these", "those", "a", "an", "the", "aboard", "about", "above", "across", "after", "against", "along", "amid", "among", "anti", "around", "as", "at", "before", "behind", "below", "beneath", "beside", "besides", "between", "beyond", "but", "by", "concerning", "considering", "despite", "down", "during", "except", "excepting",
                "excluding", "following", "for", "from", "in", "inside", "into", "like", "minus", "near", "of", "off", "on", "onto", "opposite", "outside", "over", "past", "per", "plus", "regarding", "round", "save", "since", "than", "through", "to", "toward", "towards", "under", "underneath", "unlike",
                "until", "up", "upon", "versus", "via", "with", "within", "without", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        for (String word : filterWords) {
            if (wordToCheck.equalsIgnoreCase(word)) {
                if (!pageName.toLowerCase().contains(wordToCheck.toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void makeLinkArray(String link, String text, Page page) {
        String[] linkArray = {link, text};
        if (link.substring(0, 2).equals("./") & !link.contains(":")) {
            page.getLeadLinkList().add(linkArray);
        }
    }

    String buildShortLink(String pageName) {
        if (pageName.contains(" ")) {
            return buildShortLink("/" + pageName.replace(" ", "_"));
        } else if (pageName.contains("./")) {
            return pageName.substring(1);
        } else {
            return pageName;
        }
    }



    String[][] getLimitedSortedScoreArray(String[][] sortedScoreArray, int limit) {
        String[][] tempScoreArray = new String[limit][2];
        for (int i = 0; i < limit; i++) {
            tempScoreArray[i][0] = sortedScoreArray[i][0];
            tempScoreArray[i][1] = sortedScoreArray[i][1];
        }
        return tempScoreArray;
    }
}
