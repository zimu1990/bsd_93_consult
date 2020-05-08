package com.edu.bupt.pcs.consult.utils;




import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TimeUtil {
    public  static String getStringTime(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);

    }
    /*获得当前时间*/
    public static String getCurrentTime(){
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    /*时间比大小*/
    public static int timeCompare(String t1,String t2){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c1=Calendar.getInstance();
        Calendar c2=Calendar.getInstance();
        try {
            c1.setTime(formatter.parse(t1));
            c2.setTime(formatter.parse(t2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int result=c1.compareTo(c2);
        return result;
    }

    public static void main(String[] args) throws InterruptedException {
        String str1 = getCurrentTime();
        Thread.sleep(1000);
        String str2 = getCurrentTime();
        System.out.println(timeCompare(str2,str1));

    }

}
