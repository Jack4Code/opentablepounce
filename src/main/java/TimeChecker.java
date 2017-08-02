/**
 * Created by jackg on 8/1/2017.
 */
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.*;


class TimeChecker {

    static final String TIME_TO_STOP = System.getProperty("TIME_TO_STOP","05:59:00");
    private static Date dateToStop;

    TimeChecker(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(DATE,1);

        String[] timeArr = TIME_TO_STOP.split(":");
        calendar.set(HOUR_OF_DAY,Integer.parseInt(timeArr[0]));
        calendar.set(MINUTE,Integer.parseInt(timeArr[1]));
        calendar.set(SECOND,Integer.parseInt(timeArr[2]));
        dateToStop = calendar.getTime();
    }

    boolean shouldStop(){
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        //return date.after(dateToStop) || isWeekend();
        return date.after(dateToStop);
    }

    boolean beforeFourPm(){
        int secSinceMidnight = getSsm();
        return secSinceMidnight < (16*3600);
    }

    boolean insideMarketHours(){
        int secSinceMidnight = getSsm();
        int openTime = (9*3600)+(30*60);
        int closeTime = 16*3600;
        return secSinceMidnight >= openTime && secSinceMidnight < closeTime;
    }

    private int getSsm(){
        Calendar calendar = Calendar.getInstance();
        int hh = calendar.get(HOUR_OF_DAY);
        int mm = calendar.get(MINUTE);
        int ss = calendar.get(SECOND);
        return (hh*3600)+(mm*60)+ss;
    }

    boolean isWeekend(){
        Calendar calendar = Calendar.getInstance();
        return(calendar.get(DAY_OF_WEEK)==1 || calendar.get(DAY_OF_WEEK)==7);
    }

}
