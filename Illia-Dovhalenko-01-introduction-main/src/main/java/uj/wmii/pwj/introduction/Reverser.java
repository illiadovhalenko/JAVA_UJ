package uj.wmii.pwj.introduction;

import java.util.concurrent.atomic.AtomicReference;

public class Reverser {

    public String reverse(String input) {
        if (input==null) {
            return null;
        }
        input=input.trim();
        StringBuilder stringBuilder = new StringBuilder(input);
        return stringBuilder.reverse().toString();
    }

    public String reverseWords(String input) {
        if (input==null) {
            return null;
        }
        input=input.trim();
        String[] arr=input.split(" ");
        String result="";
        for(String str: arr)
            result=str+" "+result;
        return result.trim();
    }

}
