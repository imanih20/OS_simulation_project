package com.example.OsProject.utils;

import java.util.Locale;

public class TextUtils {
    public static int getNumber(String text){
        char[] chars = text.toCharArray();
        int number=0;
        int p = 1;
        for (int i= chars.length-1; i>=0; i--){
            if (!Character.isDigit(chars[i])){
                break;
            }
            int digit = Character.digit(chars[i],10);
            number+=digit*p;
            p*=10;
        }
        return number;
    }

    public static String milliSecToTextTime(int milliSec){
        int secs = milliSec/1000;
        int sec;
        int min;
        int hour;
        if (secs<60){
            sec = secs;
            min = 0;
            hour = 0;
        }else if (secs<3600){
            min = secs/60;
            sec = secs%60;
            hour = 0;
        }else {
            hour = secs/3600;
            min = (secs%3600)/60;
            sec = secs%60;
        }
        if (hour == 0){
            return String.format(Locale.US,"%02d:%02d", min, sec);
        }
        return String.format(Locale.US,"%02d:%02d:%02d", hour, min, sec);
    }

    public static String[] getArray(String str,String regex){
        StringBuilder builder = new StringBuilder();
        String[] arr = str.split(regex);
        for (String s : arr){
            if (s.isEmpty()){
                continue;
            }
            builder.append(s).append(" ");
        }
        builder.deleteCharAt(builder.length()-1);
        return builder.toString().split(" ");
    }

    public static boolean isValidPass(String password){
        if (password.length()<8) return false;
        String alphabet="abcdefghijklmnopqrstuvwxyz";
        String numbers="0123456789";
        boolean containsNumber = false;
        boolean containsAlphabet = false;
        boolean containsSChars = false;
        for (char c : password.toCharArray()){
            if (alphabet.indexOf(c)!=-1){
                containsAlphabet = true;
            }else if (numbers.indexOf(c) != -1){
                containsNumber = true;
            }else {
                containsSChars = true;
            }
            if (containsAlphabet && containsNumber && containsSChars) return true;
        }
        return false;
    }

}
