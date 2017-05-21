package xiaobai.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by haixingcheng on 2014/5/14.
 */
public class DateUtil {
    public static String default_format="yyyy-MM-dd HH:mm:ss";
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
    public static String getDate2String(Date date,String format){
    	if(date == null){
    		return "";
    	}
        SimpleDateFormat sformat=new SimpleDateFormat(format);
        return sformat.format(date);
    }
    
    public static String getDate2String(){
    	  Calendar c = Calendar.getInstance();  
          c.setTime(new Date());  
          int day = c.get(Calendar.DATE);  
          c.set(Calendar.DATE, day-1);  
          String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());  
          return dayBefore;
    }
    public static Date getDateFromString(	String date) throws Exception{
        SimpleDateFormat sformat=new SimpleDateFormat(default_format);
        return sformat.parse(date);
    }

    public static Date getString2Date(String date, String format) throws Exception{
        SimpleDateFormat sformat=new SimpleDateFormat(format);
        return sformat.parse(date);
    }

    public static synchronized Date getStrToDate(String format, String str) {
        simpleDateFormat.applyPattern(format);
        ParsePosition parseposition = new ParsePosition(0);
        return simpleDateFormat.parse(str, parseposition);
        
    }
    public static void main(String[] args) {
    	 System.out.println(getDate2String());
	}
}
