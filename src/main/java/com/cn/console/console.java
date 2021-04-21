package com.cn.console;


public class console {
    final static boolean flag1 = true;
    final static boolean flag2 = true;

    public static void Println(Object date){
        if(flag1){
            System.out.println(date);
        }
    }
    public static void Printf(String str,Object... date){
        if(flag2){
            System.out.printf(str,date);
        }
    }
}
