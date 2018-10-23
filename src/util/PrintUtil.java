package util;

public class PrintUtil {

    public static void println(String content){
        System.out.println(content);
    }

    public static void println(){
        System.out.println();
    }

    public static void println(Object obj){
        System.out.println(obj);
    }

    public static void print(Object obj){
        System.out.print(obj);
    }

    public static void printf(String format, Object... args){
        System.out.printf(format, args);
    }
}
