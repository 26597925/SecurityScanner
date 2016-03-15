package com.x.securityscanner.libparsesofile;

/**
 * Created by ouhf1 on 16-1-8.
 */
public class KMP {

    private static KMP instance = null;

    public static KMP getInstance() {
        if(null == instance) {
            instance = new KMP();
        }
        return instance;
    }

    public int findString(char[] target, char[] pattern) {
        int sSize = target.length;
        int[] prefix = computePrefixFunction(pattern);
        int k = -1;
        for(int i=0; i<sSize; i++) {
            while(k > -1 && target[i] != pattern[k+1]) {
                k = prefix[i];
            }

            if(target[i] == pattern[k+1]) {
                k++;
            }

            if(k == pattern.length - 1) {
                return i-k;
            }
        }

        return -1;
    }

    private int[] computePrefixFunction(char[] pattern) {
        int size = pattern.length;
        int[] prefix = new int[size];

        int k = -1;
        prefix[0] = -1;

        for(int i=0; i<size; i++) {
            while (k > -1 && pattern[i] != pattern[k+1]) {
                k = prefix[i];
            }
            if(pattern[i] == pattern[k+1]) {
                k++;
            }
            prefix[i] = k;
        }

        return prefix;
    }
}
