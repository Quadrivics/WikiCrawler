package com.sscb;

public class RecursionTester {

    Integer recurse(Object data, int depth, int maxDepth) {
        System.out.println("Depth: " + depth);
        if (depth == 0) {
            // starting point
            // init data
        } else if (depth == maxDepth) {
            if (data instanceof Integer) {
                // final leaf
                return (Integer) data;
            }
            // if not integer then it has to be an array
            // so it ends definitely, not probably
            return 0;
        }
        // for everything but maxdepth
        // leaf or branch because else it has return above here
        if (data instanceof Object[]) {
            Object[] array = (Object[]) data;
            int sum = 0;
            for (int i = 0; i < array.length; i++) {
                if (array[i] instanceof Object[]) {
                    sum += recurse(array[i], depth + 1, maxDepth);
                } else {
                    sum += (Integer) array[i];
                }
            }
            return sum;
        }
        // final leaf
        return (Integer) data;
    }
    // maybe alter the wordscorelist when you go deeper
    // scrape one side and
    // add tabs multiplied with depthlevel
    // either array or string
}

