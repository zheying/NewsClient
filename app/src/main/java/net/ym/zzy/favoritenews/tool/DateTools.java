package net.ym.zzy.favoritenews.tool;

import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.text.TextUtils;
import android.util.Log;
/*
 * 时间工具类
 */
public class DateTools {
	/*
	 * 将时间戳转为字符串 ，格式：yyyy-MM-dd HH:mm
	 */
	public static String getStrTime_ymd_hm(String cc_time) {
		String re_StrTime = "";
		if(TextUtils.isEmpty(cc_time) || "null".equals(cc_time)){
			return re_StrTime;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;

	}

	/*
	 * 将时间戳转为字符串 ，格式：yyyy-MM-dd HH:mm:ss
	 */
	public static String getStrTime_ymd_hms(String cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;

	}

	/*
	 * 将时间戳转为字符串 ，格式：yyyy.MM.dd
	 */
	public static String getStrTime_ymd(String cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;
	}

	/*
	 * 将时间戳转为字符串 ，格式：yyyy
	 */
	public static String getStrTime_y(String cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;
	}

	/*
	 * 将时间戳转为字符串 ，格式：MM-dd
	 */
	public static String getStrTime_md(String cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;
	}

	/*
	 * 将时间戳转为字符串 ，格式：HH:mm
	 */
	public static String getStrTime_hm(String cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;
	}

	/*
	 * 将时间戳转为字符串 ，格式：HH:mm:ss
	 */
	public static String getStrTime_hms(String cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;
	}
	
	/*
	 * 将时间戳转为字符串 ，格式：MM-dd HH:mm:ss
	 */
	public static String getNewsDetailsDate(String cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;
	}

	/* 
	 * 将字符串转为时间戳
	 */
	public static String getTime() {
		String re_time = null;
		long currentTime = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		Date d;
		d = new Date(currentTime);
		long l = d.getTime();
		String str = String.valueOf(l);
		re_time = str.substring(0, 10);
		return re_time;
	}
	
	/*
	 * 将时间戳转为字符串 ，格式：yyyy.MM.dd  星期几
	 */
	public static String getSection(String cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd  EEEE");
//		对于创建SimpleDateFormat传入的参数：EEEE代表星期，如“星期四”；MMMM代表中文月份，如“十一月”；MM代表月份，如“11”；
//		yyyy代表年份，如“2010”；dd代表天，如“25”
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;
	}

	public static String getPublishTimeString(long publishTime){
		long current = new Date().getTime() / 1000;
		long delta = current - publishTime;
		Log.d("test", current + " " + publishTime + " " + delta);
		if (delta < 60 * 60){
			return (delta < 60 ? 1 : delta / 60) + "分钟前";
		}else if (delta < 60 * 60 * 24){
			return delta / (60 * 60) + "小时前";
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
			return sdf.format(new Date(publishTime + 1000L));
		}
	}

	public static boolean isSameDay(long time, long otherTime){
		Date date = new Date(time * 1000);
		Date otherDate = new Date(otherTime * 1000);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		Calendar otherCalendar = Calendar.getInstance();
		otherCalendar.setTime(otherDate);
		return calendar.get(Calendar.YEAR) == otherCalendar.get(Calendar.YEAR)
				&& calendar.get(Calendar.MONTH) == otherCalendar.get(Calendar.MONTH)
				&& calendar.get(Calendar.DAY_OF_MONTH) == otherCalendar.get(Calendar.DAY_OF_MONTH);
	}

//	public static String getTodayDate(){
//		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
//		String nowTime=format.format(new Date());
//		return 
//	}
}
