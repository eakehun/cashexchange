package com.hourfun.cashexchange.util;

import org.apache.commons.lang3.time.FastDateFormat;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.*;

public final class DateUtils {

    public static final String YYYYMMDDHHmmss = "yyyyMMddHHmmss";
    public static final String YYYYMMDDHHmmss_DASH = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYYMMDDHHmmss_DOT = "yyyy.MM.dd HH:mm:ss";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYYMMDD_DASH = "yyyy-MM-dd";
    public static final String YYYYMMDD_DOT = "yyyy.MM.dd";
    private static Logger logger = LoggerFactory.getLogger(DateUtils.class);
    private static Map<String, FastDateFormat> dateFormatPatternMap;

    static {
        dateFormatPatternMap = new HashMap<>();
        dateFormatPatternMap.put(YYYYMMDDHHmmss, FastDateFormat.getInstance(YYYYMMDDHHmmss));
        dateFormatPatternMap.put(YYYYMMDDHHmmss_DASH, FastDateFormat.getInstance(YYYYMMDDHHmmss_DASH));
        dateFormatPatternMap.put(YYYYMMDDHHmmss_DOT, FastDateFormat.getInstance(YYYYMMDDHHmmss_DOT));
        dateFormatPatternMap.put(YYYYMMDD, FastDateFormat.getInstance(YYYYMMDD));
        dateFormatPatternMap.put(YYYYMMDD_DASH, FastDateFormat.getInstance(YYYYMMDD_DASH));
        dateFormatPatternMap.put(YYYYMMDD_DOT, FastDateFormat.getInstance(YYYYMMDD_DOT));
    }

    private static FastDateFormat getDateFormat(String pattern) {
        if (!dateFormatPatternMap.containsKey(pattern)) {
            dateFormatPatternMap.put(pattern, FastDateFormat.getInstance(pattern));
//			logger.info("날짜 패턴 새로 생성, pattern = {}", pattern);
        }
        return dateFormatPatternMap.get(pattern);
    }

    private static FastDateFormat getDateFormat(String pattern, TimeZone timeZone) {
        if (!dateFormatPatternMap.containsKey(pattern)) {
            dateFormatPatternMap.put(pattern, FastDateFormat.getInstance(pattern, timeZone));
//			logger.info("날짜 패턴 새로 생성, pattern = {}", pattern);
        }
        return dateFormatPatternMap.get(pattern);
    }

    public static DateTypeBuilder changePattern(Date date, String pattern) {
        return new DateTypeBuilder(getDateFormat(pattern).format(date));
    }

    /**
     * Date타입 날짜를 지정한 포맷에 맞게 변경
     *
     * @param date    java.util.date or java.sql.date 사용.
     * @param pattern 미리 정의한 String 패턴 or 개발자가 임의로 작성한 날짜 패턴.
     * @return
     */
    public static String changeDateToString(Date date, String pattern) {
        return getDateFormat(pattern).format(date);
    }

    /**
     * long 타입을  지정한 포맷에 맞게 변경
     *
     * @param currentLong 입력된 시간
     * @param pattern     미리 정의한 String 패턴 or 개발자가 임의로 작성한 날짜 패턴.
     * @return
     */
    public static String changeLongToString(long currentLong, String pattern) {
        Date date = new Date();
        date.setTime(currentLong);

        return getDateFormat(pattern).format(date);
    }

    /**
     * 스트링 날짜 데이터를 Date객체로 변환
     *
     * @param strDate
     * @param pattern
     * @return 데이트 값이 pattern으로 파싱할 수 없을 경우 널값을 반환
     */
    public static Date changeStringToDate(String strDate, String pattern) {
        Date date = null;
        try {
            date = getDateFormat(pattern).parse(strDate);
        } catch (ParseException e) {
            logger.warn("날짜 변환 중 오류가 발생하였습니다. 날짜데이터:{}, 패턴:{}", strDate, pattern);
            e.printStackTrace();
        }
        return date;
    }

    public static Date changeStringToDate(String strDate, String pattern,TimeZone timeZone){
        Date date = null;
        try {
            date = getDateFormat(pattern,timeZone).parse(strDate);
        } catch (ParseException e) {
            logger.warn("날짜 변환 중 오류가 발생하였습니다. 날짜데이터:{}, 패턴:{}", strDate, pattern);
            e.printStackTrace();
        }
        return date;
    }

    public static Date changeStringToSqlDate(String strDate, String pattern, TimeZone timeZone) {
        Date date;
        try {
            date = getDateFormat(pattern, timeZone).parse(strDate);
            return toSqlDate(date);
        } catch (ParseException e) {
            logger.warn("날짜 변환 중 오류가 발생하였습니다. 날짜데이터:{}, 패턴:{}", strDate, pattern);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * java.util.Date를 java.sql.Date로 변환
     *
     * @param date
     * @return
     */
    public static java.sql.Date toSqlDate(Date date) {
        return new java.sql.Date(date.getTime());
    }

    /**
     * 숫자 값으로 Date 객체를 생성
     *
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param min
     * @param second
     * @return 입력 파라미터에 오류가 있을 경우 NULL값을 반환
     */
    public static Date toDate(int year, int month, int day, int hour, int min, int second) {
        if ((month < 1 || month > 12)
                || (day < 1 || day > 31)
                || (hour < 0 || hour > 23)
                || (min < 0 || min > 59)
                || (second < 0 || second > 59)) {
            logger.error("날짜를 생성하기 위한 파라미터 데이터에 오류가 있습니다. year:{} month:{}, day:{}, hour:{}, min:{}, second:{}", year, month, day, hour, min, second);
            return null;
        }

        DateTime dateTime = new DateTime(year, month, day, hour, min, second);

        return dateTime.toDate();
    }

    /**
     * 오늘 날짜를 기준으로 year, month, day를 뺀 날짜
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static Date minusDateFromToday(int year, int month, int day) {
        DateTime lastAnyDate = DateTime.now().minusYears(year).minusMonths(month).minusDays(day);
        return lastAnyDate.toDate();
    }

    /**
     * 오늘 날짜를 기준으로 year, month, day를 뺀 날짜
     *
     * @param year
     * @param month
     * @param day
     * @param pattern
     * @return
     */
    public static String minusDateFromToday(int year, int month, int day, String pattern) {
        DateTime lastAnyDate = DateTime.now().minusYears(year).minusMonths(month).minusDays(day);
        return changeDateToString(lastAnyDate.toDate(), pattern);
    }

    /**
     * 스트링 날짜 데이터가 날짜형식이 맞는지 확인
     *
     * @param date 년월일 정보만 입력받습니다. 예) 20160617, 2016.06.17, 2016-06-17
     * @return date값이 날짜형식이면 TRUE 아니면 FALSE
     */
    public static boolean isDate(String date) {
        String onlyNumberDate = date.replaceAll("[^0-9]", "");

        if (onlyNumberDate.length() != 8) return false;

        int year = Integer.parseInt(onlyNumberDate.substring(0, 4));
        int month = Integer.parseInt(onlyNumberDate.substring(4, 6));
        int day = Integer.parseInt(onlyNumberDate.substring(6, 8));

        if (year < 1970) return false;
        if (month < 1 || month > 12) return false;
        if (day < 1 || day > 31) return false;
        return !(month == 2 && day > 29);
    }

    /**
     * 날짜가 fromDate부터 toDate사이에 있는지 확인
     */
    public static boolean isWithinDate(Date date, Date fromDate, Date toDate) {
        return date.after(fromDate) && date.before(toDate);
    }

    public static String getToday() {
        return changeDateToString(new Date(), YYYYMMDD);
    }

    /**
     * 날짜 사이의 차이를 계산합니다.
     *
     * @param newerDate
     * @param olderDate
     * @return
     */
    public static long differenceInDays(Date newerDate, Date olderDate) {

        return (newerDate.getTime() - olderDate.getTime());
    }

    /**
     * utc 기준 날짜로 변환합니다.
     */
    public static String convertUtcDate(long epochmili, String dateFormat) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTimeInMillis(epochmili);
//        if (dateFormat.equals("YYYY-'w'ww")) {
//            FastDateFormat yearCheckFdf = FastDateFormat.getInstance("yyyy", TimeZone.getTimeZone("UTC"));
//            if (!yearCheckFdf.format(cal).equals(String.valueOf(cal.getWeekYear()))) {
//                return cal.getWeekYear() + "-w01";
//            }
//        }
        FastDateFormat fdf = FastDateFormat.getInstance(dateFormat, TimeZone.getTimeZone("UTC"));
        return fdf.format(cal);
    }

//    /**
//     * utc 기준 날짜로 변환합니다.
//     */
//    public static String convertUtcDate2(long epochmili, String dateFormat) {
//        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
//        cal.setFirstDayOfWeek(Calendar.MONDAY);
//        cal.setTimeInMillis(epochmili);
//        FastDateFormat fdf = FastDateFormat.getInstance(dateFormat, TimeZone.getTimeZone("UTC"));
//        return fdf.format(cal);
//    }

    public static String convertLocaleDate(long epochmili, String dateFormat) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTimeInMillis(epochmili);
        FastDateFormat fdf = FastDateFormat.getInstance(dateFormat);
        return fdf.format(cal);
    }

    public static class DateTypeBuilder {
        private Object basicDate;

        public DateTypeBuilder(Object basicDate) {
            this.basicDate = basicDate;
        }

        public String string() {
            if (basicDate instanceof String) {
                return (String) basicDate;
            }

            return null;
        }

        public void sqlDate() {

        }

        public void date() {

        }

        public void longNumber() {

        }
    }

}