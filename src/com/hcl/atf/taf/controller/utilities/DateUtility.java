package com.hcl.atf.taf.controller.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.Period;



public class DateUtility {

	private static final Log log = LogFactory
			.getLog(DateUtility.class);
	private static SimpleDateFormat usdateformat = new SimpleDateFormat("dd MMM yy");
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
	private static SimpleDateFormat sdfTimewithoutss = new SimpleDateFormat("HH:mm");
	private static SimpleDateFormat dateformatInSecond = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static SimpleDateFormat dateformatInSecondWithSlash = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	//Both the below declarations are same, commented the second statement
	private static SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
	//private static SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
	
	//Both the below declarations are same, commented the second statement
	private static SimpleDateFormat dateformatWithOutTimeWithSlash = new SimpleDateFormat("MM/dd/yyyy");

	//Both the below declarations are same, commented the second statement
	private static SimpleDateFormat dateformatWithOutTime = new SimpleDateFormat("dd-MM-yyyy");
	
	private static SimpleDateFormat dateformatWithMonth = new SimpleDateFormat("dd-MMM-yyyy");
	
	private static SimpleDateFormat simpledateformat = new SimpleDateFormat("dd MMM, yyyy");
	
	private static SimpleDateFormat dateformatEMMMddHHmmssZyyyy = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
	
	private static  SimpleDateFormat sdfist = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss 'GMT'z"); 
	
	private static String UTC="UTC";
	
	private static SimpleDateFormat inputFormatddMMMyyyy = new SimpleDateFormat("dd-MMM-yyyy");
	
	private static SimpleDateFormat dateformatforDateMonth = new SimpleDateFormat("MM-dd");
	
	private static SimpleDateFormat dateformatWithSlash = new SimpleDateFormat("dd/MM/yyyy");
	
	private static SimpleDateFormat dateformatWithoutHyphenInSecond = new SimpleDateFormat("yyyy,MM,dd, HH,mm");
	
	private static SimpleDateFormat dateformatWithSlashInSecond = new SimpleDateFormat("MM/dd HH:mm:ss/yyyy");
	
	public static String sdfDateformatWithOutTime(Date date) {
		return sdf.format(date);
	}
	
	public static Date getDateFromDateTime(Date date) {
		Date temp=null;
		try {
			temp = sdf.parse(sdf.format(date));
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			log.error("ERROR  ",e);
		}
		return temp;
		
	}
	
	public static Date dateFormatWithOutSeconds(String Date){
		Date temp=null;
		try {
			temp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS").parse(sdf.format((sdf.parse(Date)))+" 00:00:00.000000");
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			log.error("ERROR  ",e);
		}
		return temp;
	}
	
	public static Date dateFormatWithOutTimeStamp(String Date){
		Date temp=null;
		try {
			temp = new SimpleDateFormat("yyyy-MM-dd").parse(sdf.format((sdf.parse(Date))));
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			log.error("ERROR  ",e);
		}
		return temp;
	}
	public static Date dateFormatWithOutSecondsddMMyyyytoyyyyMMdd(String Date){
		Date temp=null;
		 SimpleDateFormat dateformatWithOutTime = new SimpleDateFormat("dd-MM-yyyy");
	      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			temp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS").parse(sdf.format((sdf.parse(Date)))+" 00:00:00.000000");
			//temp = sdf.format(dateformatWithOutTime.parse(Date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			log.error("ERROR  ",e);
		}
		return temp;
	}

	public static Date parseDateFormatOfmmddyyyy(String Date){
		Date temp=null;
		try {
			temp = new SimpleDateFormat("MM/dd/yyyy").parse(sdf.format((sdf.parse(Date))));
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			log.error("ERROR  ",e);
		}
		return temp;
	}

	public static Date dateFormatWithOutSecondsddMMyyyy(String strdate){
		Date date=null;
		try {
			if(strdate!=null && strdate.length()>0 )
			{
				date= sdf.parse(strdate);
			} 
		} catch (Exception pe) {
			throw new IllegalArgumentException(" Unable to parse the value"
					+ date + " into date " + pe);
		}
		return date;
	}

	public static String dateToStringInSecond(Date date) {
		if(date != null){
			return dateformatInSecond.format(date);
		}else{
			return null;
		}
	}
	
	public static String dateToStringInSecond(long lDate) 
	{ 
		return dateformatInSecond.format(longToDate(lDate));
	}
	
	public static Date toDateInSec(String strdate) 
	{
		Date date=null;
		try {
			if(strdate!=null && strdate.length()>0 )
			{
				date= dateformatInSecond.parse(strdate);
			} 
		} catch (Exception pe) {
			throw new IllegalArgumentException(" Unable to parse the value"
					+ date + " into date " + pe);
		}
		return date;

	}

	public static Date dateformatWithTime(String strdate) 
	{
		
		Date date=null;
		try {
			
			if(strdate!=null && strdate.length()>0 )
			{
				date=toDateInSec(dateformatInSecond.format(dateformatInSecond.parse(strdate)));
			} 
		} catch (Exception pe) {
			throw new IllegalArgumentException(" Unable to parse the value"
					+ date + " into date " + pe);
		}
		return date;

	}

	public static Date dateformatWithOutTime(String strdate) 
	{
		Date date=null;
		try {
			
			if(strdate!=null && strdate.length()>0 )
			{
				date=toDateInSec(dateformatInSecond.format(dateformatWithOutTimeWithSlash.parse(strdate)));
			} 
		} catch (Exception pe) {
			throw new IllegalArgumentException(" Unable to parse the value"
					+ date + " into date " + pe);
		}
		return date;

	}
	
	public static Date dateformatWithSlash(String strdate) 
	{
		Date date=null;
		try {
			
			if(strdate!=null && strdate.length()>0 )
			{
				date=toDateInSec(dateformatInSecond.format(dateformatWithSlash.parse(strdate)));
			} 
		} catch (Exception pe) {
			throw new IllegalArgumentException(" Unable to parse the value"
					+ date + " into date " + pe);
		}
		return date;

	}
	
	

	public static String dateformatWithSlashWithOutTime(Date strdate) 
	{
		String formattedDate = null;
		try {
			
			if(strdate!=null)
			{
				formattedDate = dateformatWithOutTimeWithSlash.format(strdate);
			} 
		} catch (Exception pe) {
			throw new IllegalArgumentException(" Unable to format the value"
					+ strdate + " into date " + pe);
		}
		return formattedDate;

	}
	
	public static Date getddmmyyyytoyyyymmddwithSec(String strdate){

		Date date=null;
		try {
			
			if(strdate!=null && strdate.length()>0 )
			{
				date=toDateInSec(dateformatInSecond.format(dateformatWithOutTime.parse(strdate)));
			} 
		} catch (Exception pe) {
			throw new IllegalArgumentException(" Unable to parse the value"
					+ date + " into date " + pe);
		}
		return date;
	}
	
	public static Date dateFromISTFormatString(String strdate) 
	{
		Date date=null;
		try {
			
			if(strdate!=null && strdate.length()>0 )
			{
				date=toDateInSec(dateformatInSecond.format(dateformatEMMMddHHmmssZyyyy.parse(strdate)));
			} 
		} catch (Exception pe) {
			throw new IllegalArgumentException(" Unable to parse the value"
					+ date + " into date " + pe);
		}
		return date;

	}
	
	public static long getTimestampFromDate(String date){
		long time =0;
		try {
			dateformatInSecond.setTimeZone(TimeZone.getTimeZone("GMT"));
			Date d= dateformatInSecond.parse(date);
			time=d.getTime()/1000;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			log.error("ERROR  ",e);
		}
		 return time;
	}

	public static String dateToStringWithSeconds1(Date date) {
		return dateformatInSecondWithSlash.format(date);
	}

	public static Date toFormatDate(String strdate) 
	{
		Date date=null;
		try {
			if(strdate!=null && strdate.length()>0 )
			{
				date= dateformatInSecondWithSlash.parse(strdate);
			} 
		} catch (Exception pe) {
			log.error("ERROR  ",pe);
			throw new IllegalArgumentException(" Unable to parse the value"
					+ date + " into date " + pe);
		}
		return date;

	}
	
	public static Date toDateInSec1(String strdate) 
	{
		Date date=null;
		try {
			if(strdate!=null && strdate.length()>0 )
			{
				date= dateformatInSecondWithSlash.parse(strdate);
			} 
		} catch (Exception pe) {
			throw new IllegalArgumentException(" Unable to parse the value"
					+ date + " into date " + pe);
		}
		return date;

	}

	public static Date toDate(String strdate) 
	{
		Date date=null;
		try {
			if(strdate!=null && strdate.length()>0 )
			{
				date= dateformat.parse(strdate);
			} 
		} catch (Exception pe) {
			throw new IllegalArgumentException(" Unable to parse the value"
					+ date + " into date " + pe);
		}
		return date;

	}

	public static String dateToString(Date date) {
		return dateformat.format(date);
	}

	public static String dateToStr(long lDate) 
	{ 
		
		return dateformat.format(longToDate(lDate));
	}

	public static String dateToString(long lDate) 
	{ 
		
		return dateformat.format(longToDate(lDate));
	}
	
	public static Date dateformatWithHyphnWithOutTime(String strdate) 
	{
		Date date=null;
		try {
			if(strdate!=null && strdate.length()>0 )
			{
				date= dateformatWithOutTime.parse(strdate);
			} 
		} catch (Exception pe) {
			throw new IllegalArgumentException(" Unable to parse the value"
					+ date + " into date " + pe);
		}
		return date;

	}

	public static Date toConvertDate(String strdate) 
	{
		Date date=null;
		try {
			if(strdate!=null && strdate.length()>0 )
			{
				date= dateformatWithOutTime.parse(strdate);
			} 
		} catch (Exception pe) {
			throw new IllegalArgumentException(" Unable to parse the value"
					+ date + " into date " + pe);
		}
		return date;

	}

	public static String dateToStringWithoutSeconds(Date date) {
		return dateformatWithOutTime.format(date);
	}
	
	public static String dateformatWithOutTime(Date date) {
		return dateformatWithOutTime.format(date);
	}
	
	public static Date toSimpleDate(String strdate) 
	{
		Date date=null;
		try {
			if(strdate!=null && strdate.length()>0 )
			{
				date= simpledateformat.parse(strdate);
			} 
		} catch (Exception pe) {
			throw new IllegalArgumentException(" Unable to parse the value"
					+ date + " into date " + pe);
		}
		return date;

	}

	public static String longToString(long lDate) { 
		return simpledateformat.format(longToDate(lDate));
	}

	public  static long dateToLong (Date date)
	{
		long lDate=0;
		if(date!=null)
		{
		 lDate= date.getTime();
		}
		return lDate;

	} 	

	public  static Date longToDate (long lDate)
	{
		Date dt=null;
		try
		{
			if(lDate != 0) dt = new Date(lDate);
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException(" Unable to convert the long value: "
					+ lDate + " into date " + e);			
		}
		return dt;

	}
	
	public static Date getCurrentTime(){
		return new Date(System.currentTimeMillis());
	}
	
	public static Date getWeekStartDate(int weekNo, int year) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM dd yyyy");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.WEEK_OF_YEAR, weekNo);        
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); 
		return cal.getTime();
	}
	
	public static Date getWeekStartDateFromJanMonday(int weekNo, int year) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM dd yyyy");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); 
		cal.set(Calendar.WEEK_OF_YEAR, 1);
		cal.set(Calendar.YEAR, year);
		if(cal.get(Calendar.MONTH) != Calendar.JANUARY){
			weekNo = weekNo + 1;
		}
		cal.set(Calendar.WEEK_OF_YEAR, weekNo);    
		cal.set(Calendar.YEAR, year);
		return cal.getTime();
	}
	
	public static int getYearOfCurrentDate() {
		
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	public static int getWeekOfYear() {
		
		return Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
	}

	public static List<String> getDateNamesOfWeek(int weekNo) {
		
		List<String> datesOfWeek = new ArrayList<String>();

		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM YY");
		Calendar cal = Calendar.getInstance();

		cal.set(Calendar.WEEK_OF_YEAR, weekNo);        
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		SimpleDateFormat df = new SimpleDateFormat("dd MMM YY");
		for (int i = 0; i < 7; i++) {
			datesOfWeek.add(df.format(cal.getTime()));
		    cal.add(Calendar.DATE, 1);
		}
		return datesOfWeek;
	}

	public static List<Date> getDatesOfWeek(int weekNo) {
		
		List<Date> datesOfWeek = new ArrayList<Date>();

		Calendar cal = Calendar.getInstance();

		cal.set(Calendar.WEEK_OF_YEAR, weekNo);        
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

		for (int i = 0; i < 7; i++) {
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			datesOfWeek.add(cal.getTime());
		    cal.add(Calendar.DATE, 1);
		}
		return datesOfWeek;
	}

	public static Date getDateForDayOfWeek(int weekNo, int dayNo) {
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.WEEK_OF_YEAR, weekNo);        
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.add(Calendar.DATE, dayNo);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}
	
	public static String getDateStringForDayOfWeek(int weekNo, int dayNo) {
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.WEEK_OF_YEAR, weekNo);        
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.add(Calendar.DATE, dayNo);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		SimpleDateFormat df = new SimpleDateFormat("dd MMM YY");
		return df.format(cal.getTime());
	}

	public static int getDayOfWeek(Date date) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		SimpleDateFormat df = new SimpleDateFormat("dd MMM");
		return cal.get(Calendar.DAY_OF_WEEK);
	}
	
	public static int getYearOfDate(Date date) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}
	
	public static int getMonthOfDate(Date date) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return (cal.get(Calendar.MONTH)+1);
	}
	
	public static int getDateOfDate(Date date) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DATE);
	}

	public static Date getCurrentDate(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}
	
	public static String getCurrentDateInddmmyyyy(){
		
		Date date= new Date();
		String modifiedDate= new SimpleDateFormat("yyyy-MM-dd").format(date);
		return  modifiedDate;
	}
	
	public static Date getCurrentDateWithTime(){
		
		Date date= new Date();
		String modifiedDate= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		
		Date dateFormated =null;
		try {
			dateFormated = dateformatInSecond.parse(modifiedDate);
		} catch (ParseException e) {
			log.error("ERROR  ",e);
		}

		return  dateFormated;
	}
	public static Date getCurrentDateInddmmyyyyWithTime(){
		
		Date date= new Date();
		String modifiedDate= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		
		Date dateFormated =null;
		try {
			dateFormated = dateformatInSecond.parse(modifiedDate);
		} catch (ParseException e) {
			log.error("ERROR  ",e);
		}
		return  dateFormated;
	}

	public static int getWeekNumberOfDateOnAYear(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_YEAR);
	}
	
	public static int getDayNumberOfDateOnAYear(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_YEAR);
	}
	
	public static long getTimestampFromDate(Date date){
		long time =0;
		try {
			time=date.getTime()/1000;
		//	
		} catch (Exception e) {
			log.error("ERROR  ",e);
		}
		 return time;
	}
	
	public static String getDayNameForDayNumber(int weekDayNumber){
		String dayName = null;
		switch(weekDayNumber){
			case 1: 
				dayName = "Sunday";
				break;
			case 2: 
				dayName = "Monday";
				break;
			case 3:
				dayName = "Tuesday";
				break;
			case 4: 
				dayName = "Wednesday";
				break;
			case 5: 
				dayName = "Thursday";
				break;
			case 6: 
				dayName = "Friday";
				break;
			case 7: 
				dayName = "Saturday";
				break;
		}
		return dayName; 
	}
	
	public static Date getWeekStart(Date date)
	{    
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(date);
		int dayNumber = cal.get(Calendar.DAY_OF_WEEK);
		if (dayNumber == 1)// Sunday
			cal.add(Calendar.DATE, -6); // Get Previous Monday
		else	
			cal.add(Calendar.DATE, -(dayNumber-2));
		return cal.getTime();
	}

	public static Date dateFromIST(String strdate) 
	{
		
		Date date=null;
		try {
			
			if(strdate!=null && strdate.length()>0 )
			{
				date=toDateInSec(dateformatInSecond.format(sdfist.parse(strdate)));
			} 
		} catch (Exception pe) {
			throw new IllegalArgumentException(" Unable to parse the value"
					+ date + " into date " + pe);
		}
		return date;

	}

	public static Date getPrevDate(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, -1);  //number of days to add
		return cal.getTime(); 
	}
	
	public static Date getAnyPrevDate(Date date, int numberOfDays){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, numberOfDays);  //number of days to add
		return cal.getTime(); 
	}

	public static Date getNextDate(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 1);  //number of days to add
		return cal.getTime(); 
	}
	
	public static Date addHoursToDate(Date date, int hours){
		Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    cal.add(Calendar.HOUR_OF_DAY, hours); // number of hours to add
	    return cal.getTime();
	}    

	public static String getTimeStampinHHmmss(Date date){
		return sdfTime.format(date);
	}
	
	public static String getTimeStampinHHmm(Date date){
		return sdfTimewithoutss.format(date);
	}
	
	public static Date getStringconvertedTime(Date modifieddate, String timeStr){
		String[] timeStrArray=timeStr.split(":");
		modifieddate.setHours(Integer.parseInt(timeStrArray[0]));
		modifieddate.setMinutes(Integer.parseInt(timeStrArray[1]));
		return modifieddate;
	}
	
	public static String convertTimeInHoursMinutes(Integer Hours, Integer Minutes) {
		// Here Minutes can be more than 60minutes as well, still conversion happens into Hours and Minutes
		Integer overAllMinutes = null;
		if (Hours == null && Minutes == null) {
			return null;
		}
		if (Hours != null)
			overAllMinutes = Hours * 60;
		if (Minutes != null) {
			if (overAllMinutes == null)
				overAllMinutes = Minutes;
			else
				overAllMinutes = overAllMinutes + Minutes;

		}
		Integer totalTimeConvertedInHours = overAllMinutes / 60;
		Integer totalTimeConvertedInMinutes = overAllMinutes % 60;
		
		String convertedTimeInHoursMinutes = null;
		
		if (totalTimeConvertedInHours.intValue() < 10) 
			convertedTimeInHoursMinutes = "0" + totalTimeConvertedInHours;
		else 
			convertedTimeInHoursMinutes = "" + totalTimeConvertedInHours;
		
		if (totalTimeConvertedInMinutes.intValue() < 10) 
			convertedTimeInHoursMinutes = convertedTimeInHoursMinutes + ":" + "0" + totalTimeConvertedInMinutes;
		else 
			convertedTimeInHoursMinutes = convertedTimeInHoursMinutes + ":" + totalTimeConvertedInMinutes;
		
		return convertedTimeInHoursMinutes;
	}

	public static Integer convertTimeInMinutes(Integer Hours, Integer Minutes) {
		// Here Minutes can be more than 60minutes as well, still conversion happens into total minutes
		Integer overAllMinutes = null;
		if (Hours == null && Minutes == null) {
			return null;
		}
		if (Hours != null)
			overAllMinutes = Hours * 60;
		if (Minutes != null) {
			if (overAllMinutes == null)
				overAllMinutes = Minutes;
			else
				overAllMinutes = overAllMinutes + Minutes;

		}
		return overAllMinutes;
	}

	public static String convertTimeInHoursDecimalFormatMinutes(Integer Hours, Integer Minutes) {
		Integer overAllMinutes = null;
		if (Hours == null && Minutes == null) {
			return null;
		}
		if (Hours != null)
			overAllMinutes = Hours * 60;
		if (Minutes != null) {
			if (overAllMinutes == null)
				overAllMinutes = Minutes;
			else
				overAllMinutes = overAllMinutes + Minutes;

		}
		Integer totalTimeConvertedInHours = overAllMinutes / 60;
		Integer totalTimeConvertedInMinutes = overAllMinutes % 60;
		Integer totalTimeConvertedInDecimalFormatMinutes = (totalTimeConvertedInMinutes * 100) / 60;
		return totalTimeConvertedInHours + "."
				+ totalTimeConvertedInDecimalFormatMinutes;
	}
	
	public static HashMap<String,Integer> convertTimeInHoursMinutesHashMap(Integer Hours, Integer Minutes) {
		// Here Minutes can be more than 60minutes as well, still conversion happens into Hours and Minutes
		Integer overAllMinutes = null;
		if (Hours == null && Minutes == null) {
			return null;
		}
		if (Hours != null)
			overAllMinutes = Hours * 60;
		if (Minutes != null) {
			if (overAllMinutes == null)
				overAllMinutes = Minutes;
			else
				overAllMinutes = overAllMinutes + Minutes;

		}
		Integer totalTimeConvertedInHours = overAllMinutes / 60;
		Integer totalTimeConvertedInMinutes = overAllMinutes % 60;
		HashMap<String,Integer> timeMap =  new HashMap<String,Integer>();
		timeMap.put("Hours", totalTimeConvertedInHours);
		timeMap.put("Minutes", totalTimeConvertedInMinutes);
		return timeMap;
	}
	
	public static Date toDateInYYYYMMdd(String strdate) 
	{
		Date date=null;
		try {
			if(strdate!=null && strdate.length()>0 )
			{
				date= sdf.parse(strdate);
			} 
		} catch (Exception pe) {
			throw new IllegalArgumentException(" Unable to parse the value"
					+ date + " into date " + pe);
		}
		return date;

	}
	
	public static String getCurrentTimehhmmss(){
		SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
		Date now = new Date();
	    String strTime = sdfTime.format(now);
	    strTime=strTime.replace(":", "");
	    return strTime;
	}
	
	public static void main(String[] args) throws ParseException {
		Locale locale= new Locale("en","UK");
		Locale locale1= new Locale("en","US");
		String dateS="2014-02-28 07:25:25";
		Long time=1393572325000L;
		convertToUTC(locale,dateS);
		String UTCDate = "2014-02-28 07:25:25";
		stringToDate(UTCDate);
		convertUTCToLocale(locale,stringToDate(UTCDate));
	}
	public static long DateDifference(String fromDate,String toDate) {
		long diffDays=0;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	 
			Date d1 = null;
			Date d2 = null;
	 
			try {
				d1 = format.parse(fromDate);
				d2 = format.parse(toDate);
	 
				long diff = d2.getTime() - d1.getTime();
				//

				long diffSeconds = diff / 1000 % 60;
				long diffMinutes = diff / (60 * 1000) % 60;
				long diffHours = diff / (60 * 60 * 1000) % 24;
				diffDays = diff / (24 * 60 * 60 * 1000);

	 
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
		return diffDays;
	}

	public static long DateDifferenceInSeconds(String fromDate,String toDate) {
		long diffDays=0;
		long diff=0;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	 
			Date d1 = null;
			Date d2 = null;
	 
			try {
				d1 = format.parse(fromDate);
				d2 = format.parse(toDate);
	 
				diff = d2.getTime() - d1.getTime();
				if(diff==0){
					diff=24 * 60 * 60 * 1000;
				}

				long diffSeconds = diff / 1000 % 60;
				long diffMinutes = diff / (60 * 1000) % 60;
				long diffHours = diff / (60 * 60 * 1000) % 24;
				diffDays = diff / (24 * 60 * 60 * 1000);

	 
			} catch (Exception e) {
				log.error("ERROR  ",e);
			}
			return diff;

	}

	
	public static long DateDifferenceInMinutes(Date fromDate, Date toDate) {
		long diffMinutes=0;
 
		Date d1 = null;
		Date d2 = null;
 
		try {
			d1 = dateformatInSecond.parse(dateformatInSecond.format(fromDate));
			d2 = dateformatInSecond.parse(dateformatInSecond.format(toDate));
 
			long diff = d2.getTime() - d1.getTime();
			diffMinutes = diff / (60 * 1000);
		} catch (Exception e) {
			log.error("ERROR  ",e);
		}
		return diffMinutes;
	}

	
	public static Date addOnlyWorkingDays(Date date,int days){
	    Calendar calendar = Calendar.getInstance();
	   // Date date=calendar.getTime();
	    calendar.setTime(date);
	    

	    SimpleDateFormat s;
	    s=new SimpleDateFormat("YYYY-MM-dd");


	    for(int i=0;i<days;)
	    {
	        calendar.add(Calendar.DAY_OF_MONTH, 1);
	        if(calendar.get(Calendar.DAY_OF_WEEK)<=5)
	        {
	            i++;
	        }

	    }
	    date=calendar.getTime(); 
	    s=new SimpleDateFormat("YYYY-MM-dd");
	    return date;
	}
	
	public static String getCurrentTimeInYYYYMMDD() throws ParseException{
		Date date=new Date(System.currentTimeMillis());
		
		String d1=sdf.format(dateformatEMMMddHHmmssZyyyy.parse(date.toString()));
		return d1;
	}
	
	public static String convertToUTC(Locale locale){
		TimeZone timeZone = TimeZone.getTimeZone(UTC);
		Calendar calendar = Calendar.getInstance(timeZone);
		SimpleDateFormat simpleDateFormat = 
		       new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale);
		simpleDateFormat.setTimeZone(timeZone);
		String UTC=simpleDateFormat.format(calendar.getTime());
	
		return UTC;
	}
	
	public static Date convertToUTC(Locale locale,String dateS) throws ParseException{
		TimeZone timeZone = TimeZone.getTimeZone(UTC);
		Date date=stringToDate(dateS);
		Calendar calendar = Calendar.getInstance(timeZone);
		SimpleDateFormat simpleDateFormat = 
		       new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale);
		simpleDateFormat.setTimeZone(timeZone);
		String UTC=simpleDateFormat.format(date.getTime());
		Date UTCDate=stringToDate(UTC);
		return UTCDate;
	}
	
		
	public static String convertUTCToLocale(Locale locale,Date dateInUTC) throws ParseException{
		
		TimeZone tz = TimeZone.getTimeZone(locale.getDisplayCountry());
		TimeZone timeZone = TimeZone.getTimeZone(tz.getID());
		dateformatInSecond.setTimeZone(TimeZone.getTimeZone(UTC));
	    SimpleDateFormat localeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale);
	    localeFormat.setTimeZone(timeZone);
	    Date timestamp = dateformatInSecond.parse(dateformatInSecond.format(dateInUTC));
	    String localeDate = localeFormat.format(timestamp);
		return localeDate;
	}
	
	public static String convertUTCToLocale(Locale locale,String dateSInUTC) throws ParseException{
		TimeZone tz = TimeZone.getTimeZone(locale.getDisplayCountry());
		TimeZone timeZone = TimeZone.getTimeZone(tz.getID());
		
		dateformatInSecond.setTimeZone(TimeZone.getTimeZone(UTC));
	    SimpleDateFormat localeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale);
	    localeFormat.setTimeZone(timeZone);
	    Date timestamp = dateformatInSecond.parse(dateformatInSecond.format(stringToDate(dateSInUTC)));
	    String localeDate = localeFormat.format(timestamp);
		return localeDate;
	}
	
	public static Date stringToDate(String date){
		Date temp=null;
		try {
			temp = dateformatInSecond.parse(date);
		} catch (ParseException e) {
			log.error("ERROR  ",e);
		}
		log.info("temp>>"+temp);
		return temp;
	}
	
	public static Date strInddMMMyyyyFormatToDate(String strdate) 
	{
		Date date=null;
		try {
			if(strdate!=null && strdate.length()>0 )
			{
				date= inputFormatddMMMyyyy.parse(strdate);
			} 
		} catch (Exception pe) {
			throw new IllegalArgumentException(" Unable to parse the value"
					+ strdate + " into date " + pe);
		}
		return date;
	}
	
	
		
		public static Date getFirstDateOfMonth(String date) {
			Date monthStartDate = null;
			if(date != null){
				String startDate = "01"+date.substring(2, 11);
				monthStartDate = strInddMMMyyyyFormatToDate(startDate);
				log.info("Today: " + sdf.format(monthStartDate));  
			}
			return monthStartDate;
		}
		
		
		public static Date getLastDateOfMonth(String strDate) {
			Date date = strInddMMMyyyyFormatToDate(strDate);
			Calendar calendar = Calendar.getInstance();  
	        calendar.setTime(date);  
	        calendar.add(Calendar.MONTH, 1);  
	        calendar.set(Calendar.DAY_OF_MONTH, 1);  
	        calendar.add(Calendar.DATE, -1);  
	        Date lastDayOfMonth = calendar.getTime();  
	        log.info("Today            : " + sdf.format(date));  
	        log.info("Last Day of Month: " + sdf.format(lastDayOfMonth)); 
			return calendar.getTime();
		}
		
		
		public static int getWeekNumberOfDate(Date workDate){
			Calendar cal = Calendar.getInstance();
			cal.setTime(workDate);
			int week = cal.get(Calendar.WEEK_OF_YEAR);
			return week;
		}

		public static Date addMinutesToDate(Date date, int minutes){
			Calendar cal = Calendar.getInstance();
		    cal.setTime(date);
		    cal.add(Calendar.MINUTE, minutes); // number of minutes to add
		    return cal.getTime();
		}
		
		public static int findWeekEndsBetWeenDates(Date startDate, Date endDate){
			LocalDate stDate = new LocalDate(startDate);
			LocalDate edDate = new LocalDate(endDate);
			int counts=0;
			int weekends=0;
			for (LocalDate date = stDate; date.isBefore(edDate); date = date.plusDays(1))
			{
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date.toDate());
				int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
				if(dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
					++weekends;
				}
			 ++counts;   
			 
			}
		return weekends;
		}
		public static int dateGreaterorLesserThan(Date date1, Date date2){
			if(date1.equals(date2)){
				log.info("Date1 is equal Date2");
                return 0;
            }
			if(date1.after(date2)){
          //      
				log.info("Date1 Greater than Date2");
				
               return 1; 
            }
            if(date1.before(date2)){
            	log.info("Date1 Lesser than Date2");
                return 2;
            }
            
			return 0;
		}
		public static int dateWithInRange(Date sDate, Date eDATE, Date testDate){		
			if(testDate.equals(sDate)){
				log.info("testDate==sDate");
                return 1;
            }else if(testDate.equals(eDATE)){
            	log.info("testDate==sDate");
                return 1;
            }else if((testDate.after(sDate)) && (testDate.before(eDATE))){         
            	log.info("testDate is with in range");
               return 1; 
            }else{
            	return 0;
            }            
            
		}
		
		
		public static Date formatedDateWithOutTime(Date date){
		Date dateWithOutTime;
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime( date );
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);

	    dateWithOutTime = calendar.getTime();

	    return dateWithOutTime;
	    
	}
		
		public static Date formatedDateWithMaxTime(Date date){
			Date dateWithOutTime;
		    Calendar calendar = Calendar.getInstance();
		    calendar.setTime( date );
		    calendar.set(Calendar.HOUR_OF_DAY, 23);
		    calendar.set(Calendar.MINUTE, 59);
		    calendar.set(Calendar.SECOND, 59);
		    calendar.set(Calendar.MILLISECOND, 0);

		    dateWithOutTime = calendar.getTime();

		    return dateWithOutTime;
		    
		}
		
		public static Date formatedDateWithMaxYear(Date date){
			Date dateWithOutTime;
		    Calendar calendar = Calendar.getInstance();
		    calendar.setTime( date );
		    calendar.set(Calendar.YEAR,2099);
		    calendar.set(Calendar.HOUR_OF_DAY, 23);
		    calendar.set(Calendar.MINUTE, 59);
		    calendar.set(Calendar.SECOND, 59);
		    calendar.set(Calendar.MILLISECOND, 0);

		    dateWithOutTime = calendar.getTime();

		    return dateWithOutTime;
		    
		}
		
		public static String dateformatWithDateMonth(Date date) {
			return dateformatforDateMonth.format(date);
		}
		public static String getDefectAge(Date completeBy){
			String remainghrsMins="";

			try {
				
				//in milliseconds
				long diff = new Date().getTime() - completeBy.getTime();

				long diffSeconds = diff / 1000 % 60;
				long diffMinutes = diff / (60 * 1000) % 60;
				long diffHours = diff / (60 * 60 * 1000) % 24;
				long diffDays = diff / (24 * 60 * 60 * 1000);
				remainghrsMins=diffDays+ "D "+diffHours+ "H "+diffMinutes+"M "+diffSeconds+"S";
			} catch (Exception e) {
				e.printStackTrace();
			}
			return remainghrsMins;
		}
		public static String displayHoursMinutesFormat(Date completeBy){
			String remainghrsMins="";
			if(completeBy != null) {
			  Interval interval =
		               new Interval(new Date().getTime(), completeBy.getTime());
			  Period period = interval.toPeriod();
			  		if(period.getDays() >0){
			  			remainghrsMins=(period.getDays()+"D "+ period.getHours()+"H "+period.getMinutes()+"M").toString();
			  		}else {
			  			remainghrsMins=(period.getHours()+"H "+period.getMinutes()+"M").toString();
			  		}
			}else{
				remainghrsMins = "--";
			}
			return remainghrsMins;
		}
		
		public static Integer getCalendarHoursBetweenDates(Date startDate, Date endDate){
			Integer hoursBetweenDates = 0;
			try{
				if(startDate != null && endDate != null){
					Long timeDifferenceInMilliSecond = 0L;
					
					if(startDate.getTime() > endDate.getTime()){
						timeDifferenceInMilliSecond = startDate.getTime() - formatedDateWithMaxTime(endDate).getTime();
					}else{
						timeDifferenceInMilliSecond = formatedDateWithMaxTime(endDate).getTime() - startDate.getTime();
					}
					hoursBetweenDates = (int) (timeDifferenceInMilliSecond / (60 * 60 * 1000));
				}
				if(hoursBetweenDates < 0){
					hoursBetweenDates = hoursBetweenDates * -1;
				}
			}catch(Exception ex){
				log.error("Error in getCalendarHoursBetweenDates - ", ex);
			}
			return hoursBetweenDates;
		}
		
		public static Date getDateFromString(String dateInString){
			Date convertedDate = null;
			if(dateInString == null || dateInString.trim().isEmpty()){
				return convertedDate;
			}
			try{
				convertedDate = usdateformat.parse(dateInString);
				return convertedDate;
			}catch(Exception ex){}
			try{
				convertedDate = sdf.parse(dateInString);
				return convertedDate;
			}catch(Exception ex){}
			try{
				convertedDate = sdfTime.parse(dateInString);
				return convertedDate;
			}catch(Exception ex){}
			try{
				convertedDate = dateformatInSecond.parse(dateInString);
				return convertedDate;
			}catch(Exception ex){}
			try{
				convertedDate = dateformatInSecondWithSlash.parse(dateInString);
				return convertedDate;
			}catch(Exception ex){}
			try{
				convertedDate = dateformat.parse(dateInString);
				return convertedDate;
			}catch(Exception ex){}
			try{
				convertedDate = dateformatWithOutTimeWithSlash.parse(dateInString);
				return convertedDate;
			}catch(Exception ex){}
			try{
				convertedDate = dateformatWithOutTime.parse(dateInString);
				return convertedDate;
			}catch(Exception ex){}
			try{
				convertedDate = dateformatWithMonth.parse(dateInString);
				return convertedDate;
			}catch(Exception ex){}
			try{
				convertedDate = simpledateformat.parse(dateInString);
				return convertedDate;
			}catch(Exception ex){}
			try{
				convertedDate = dateformatEMMMddHHmmssZyyyy.parse(dateInString);
				return convertedDate;
			}catch(Exception ex){}
			try{
				convertedDate = sdfist.parse(dateInString);
				return convertedDate;
			}catch(Exception ex){}
			try{
				convertedDate = inputFormatddMMMyyyy.parse(dateInString);
				return convertedDate;
			}catch(Exception ex){}
			try{
				convertedDate = dateformatforDateMonth.parse(dateInString);
				return convertedDate;
			}catch(Exception ex){}
			return convertedDate;
		}
		
		
		public static int compareDateTimeRange(Date date1, Date date2){
			return date1.compareTo(date2);
		}
		
		public static long dayDifference(Date fromDate,Date toDate) {
			long diffDays=0;
				try {
		 
					long diff = toDate.getTime() - fromDate.getTime();
					diffDays = diff / (24 * 60 * 60 * 1000);
				} catch (Exception e) {
					log.error("ERROR  ",e);
				}
			return diffDays;
		}
		
		
		public static Date dateFormatToDDMMYYYY(String strdate) 
		{
			Date date=null;
			try {
				if(strdate!=null && strdate.length()>0 )
				{
					date= dateformatWithSlash.parse(strdate);
				} 
			} catch (Exception pe) {
				throw new IllegalArgumentException(" Unable to parse the value"
						+ date + " into date " + pe);
			}
			return date;

		}
		
		public static String dateToStringWithoutHyphenSecond(Date date) {
			return dateformatWithoutHyphenInSecond.format(date);
		}
		
		//Added for new SimpleDate Format type		
		public static Date toDateSlashInSec(String strdate) {
			Date date=null;
			try {
				if(strdate!=null && strdate.length()>0 )
				{
					date= dateformatWithSlashInSecond.parse(strdate);
				} 
			} catch (Exception pe) {
				throw new IllegalArgumentException(" Unable to parse the value"
						+ date + " into date " + pe);
			}
			return date;
		}
		
		
		public static String getTestCaseAge(Date date){
			String remainghrsMins="";

			try {
				
				//in milliseconds
				long diff = new Date().getTime() - date.getTime();

				long diffSeconds = diff / 1000 % 60;
				long diffMinutes = diff / (60 * 1000) % 60;
				long diffHours = diff / (60 * 60 * 1000) % 24;
				long diffDays = diff / (24 * 60 * 60 * 1000);
				remainghrsMins=diffDays+ "D ";

			} catch (Exception e) {
				e.printStackTrace();
			}
			return remainghrsMins;
		}
		
		
		public static Date dateWithTimeFormat(String strdate) 
		{
			SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			Date date=null;
			try {
				if(strdate!=null && strdate.length()>0 )
				{
					date= dateformat.parse(strdate);
				} 
			} catch (Exception pe) {
				throw new IllegalArgumentException(" Unable to parse the value"
						+ date + " into date " + pe);
			}
			return date;

		}
		
		public static Date dateNewFormatWithOutTime(String strdate) {
			Date date=null;
			try {
				
				if(strdate!=null && strdate.length()>0 )
				{
					date=toDateInSec(dateformatInSecond.format(sdf.parse(strdate)));
				} 
			} catch (Exception pe) {
				throw new IllegalArgumentException(" Unable to parse the value "
						+ strdate + " into date " + pe);
			}
			return date;
		}
		
		
		public static Integer ActivityDateValidation(Date actStDate, Date actEndDate,
				Date wpStDate, Date wpEndDate) {		
			Integer result = 0;
			try {
				log.info("actStDate" + actStDate);
				log.info("actEndDate" + actEndDate);
				log.info("wpStDate" + wpStDate);
				log.info("wpEndDate" + wpEndDate);
				
				actStDate=DateUtility.formatedDateWithOutTime(actStDate);
				actEndDate=DateUtility.formatedDateWithOutTime(actEndDate);
				wpStDate=DateUtility.formatedDateWithOutTime(wpStDate);
				wpEndDate=DateUtility.formatedDateWithOutTime(wpEndDate);
				
				
				if((actStDate.compareTo(wpStDate)<0)||(actStDate.compareTo(wpEndDate)>0)){
					result = 1;
				}
				else if((actEndDate.compareTo(wpStDate)<0)||(actEndDate.compareTo(wpEndDate)>0)){
					result = 2;
				}			
			}

			catch (Exception ie) {
				log.error("Activity Date compare error", ie);
				
			}
			return result;

		}
		
		/*
		 * Utility function to calculate working hours (Monday to Friday) between two dates
		 */
		public static Integer getWorkingHoursBetweenTwoDates(Date startDate, Date endDate, Integer workingHoursInDay) {
			
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(startDate);        

			Calendar endCal = Calendar.getInstance();
			endCal.setTime(endDate);

			if (workingHoursInDay == null || workingHoursInDay <= 0)
				workingHoursInDay = 9;
			
			int workDays = 0;
			int workingHours = 0;

			//Return 0 if start and end are the same
			if (startCal.getTimeInMillis() >= endCal.getTimeInMillis()) {
				return 0;
			}
			
			if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
				startCal.setTime(endDate);
				endCal.setTime(startDate);
			}

			do {
				//excluding start date
				startCal.add(Calendar.DAY_OF_MONTH, 1);
				if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
					++workDays;
				}
			} while (startCal.getTimeInMillis() < endCal.getTimeInMillis()); //excluding end date

			//The intervening days will have full working hours in the day
			workingHours = workDays * workingHoursInDay;
			//Calculate the working hours on the start date
			
			/*int startDateHours = 0;
			int startHour = startDate.getHours();
			if (startHour > 17)
				startDateHours = 0;
			else if (startHour < 9)
				startDateHours = 9;
			else 
				startDateHours = 9 - (startHour - 9);
				
			int endDateHours = 0;
			int endHour = endDate.getHours();
			if (endHour > 17)
				endDateHours = 9;
			else if (endHour < 9)
				endDateHours = 0;
			else 
				endDateHours = endHour;*/

			return workingHours;
		}
		
		public static Date formatedDateWithMaxDateWithTime(Date date){
			Date dateWithOutTime;
			Calendar calendar = Calendar.getInstance();
			calendar.setTime( date );
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 0);

			dateWithOutTime = calendar.getTime();

			return dateWithOutTime;

		}
}