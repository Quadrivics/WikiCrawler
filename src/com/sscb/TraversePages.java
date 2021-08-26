package com.sscb;

public class TraversePages {

    DataBuilder db;

    TraversePages (DataBuilder db) {
        this.db = db;
    }

    Object recurse(Object data, int depth, int maxDepth) {
        if (depth == 0) {
            data = db.crawlPage((String) data);
        } else if (depth == maxDepth) {
            if (data instanceof Page) {
                System.exit(0);
//                return (Page) data;
            }
        }
        if (data instanceof Object[]) {
            Object[] array = (Object[]) data;
            for (int i = 0; i < array.length; i++) {
                if (array[i] instanceof Object[]) {
                    recurse(array[i], depth + 1, maxDepth);
                }
            }
            for (Page page : (Page[]) array) {
                db.startCrawl(page.getShortUrl());
            }
            return (Page[]) data;
        }
        System.out.println("- Depth: " + depth + " ------------------------------");
        return (Page) data;
    }
    }

