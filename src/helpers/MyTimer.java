package com.company;

import java.util.HashMap;

public class MyTimer {
    // just a debug class
    private static HashMap<Integer, Long> times = new HashMap<>();
    public static void Start(){
        Start(0);
    }

    public static void Start(int i){
        Long startTime = times.get(i);
        if(startTime == null){
            startTime = new Long(System.currentTimeMillis());
            times.put(i, startTime);
        }else{
            startTime = System.currentTimeMillis();
            times.put(i, startTime);
        }
    }

    public static long getTimeElapsed(){
        return getTimeElapsed(0);
    }
    public static long getTimeElapsed(int i){
        Long startTime = times.get(i);
        if(startTime == null){
            startTime = new Long(System.currentTimeMillis());
            times.put(i, startTime);
            return 0;
        }
        return (System.currentTimeMillis() - startTime);
    }

    public static void printTimeElapsed(){
        printTimeElapsed(0);
    }
    public static void printTimeElapsed(String name){
        printTimeElapsed(0, name);
    }
    public static void printTimeElapsed(int i){
        printTimeElapsed(i,"");
    }
    public static void printTimeElapsed(int i, String name){
        name += name.length() < 1 ? "T" : " t";
        System.out.println(name + "ime elapsed : " + getTimeElapsed(i) + "ms");
    }

    public static void printTime(long startTime, String name){
        System.out.println(name + " Time elapsed : " + (System.currentTimeMillis() - startTime) + "ms");
    }
}
