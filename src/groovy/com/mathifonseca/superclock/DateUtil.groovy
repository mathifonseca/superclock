package com.mathifonseca.superclock

class DateUtil {

    static String getCurrentTimeWithOffset(Integer offset) {

        TimeZone utc = TimeZone.getTimeZone('UTC')

        TimeZone.default = utc

        Calendar calendar = new GregorianCalendar(utc)

        calendar.add(Calendar.MINUTE, offset ?: 0)

        return calendar.time.format('yyyy-MM-dd HH:mm')

    }

}
