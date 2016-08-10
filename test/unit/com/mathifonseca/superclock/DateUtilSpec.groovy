package com.mathifonseca.superclock

import com.mathifonseca.superclock.test.UnitTestUtil
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

class DateUtilSpec extends Specification {

    private String dateFormat = 'yyyy-MM-dd HH:mm'

    private Date getCurrentUtc() {
        TimeZone utc = TimeZone.getTimeZone('UTC')
        TimeZone.default = utc
        return new GregorianCalendar(utc).time
    }

    void 'test offset is zero'() {
        given:
            Integer offset = 0

        when:
            String result = DateUtil.getCurrentTimeWithOffset(offset)
            Date parsedResult = Date.parse(dateFormat, result)

        then:
            result
            result == currentUtc.format(dateFormat)
            parsedResult[Calendar.YEAR] == currentUtc[Calendar.YEAR]
            parsedResult[Calendar.MONTH] == currentUtc[Calendar.MONTH]
            parsedResult[Calendar.DAY_OF_MONTH] == currentUtc[Calendar.DAY_OF_MONTH]
            parsedResult[Calendar.HOUR_OF_DAY] == currentUtc[Calendar.HOUR_OF_DAY]
            parsedResult[Calendar.MINUTE] == currentUtc[Calendar.MINUTE]

    }

    void 'test offset null is same as zero'() {
        given:
            Integer offset = null

        when:
            String result = DateUtil.getCurrentTimeWithOffset(offset)

        then:
            result
            result == DateUtil.getCurrentTimeWithOffset(0)

    }

    void 'test offset is -180'() {
        given:
            Integer offset = -180

        when:
            String result = DateUtil.getCurrentTimeWithOffset(offset)
            Date parsedResult = Date.parse(dateFormat, result)

        then:
            result
            result != currentUtc.format(dateFormat)
            parsedResult[Calendar.YEAR] == currentUtc[Calendar.YEAR]
            parsedResult[Calendar.MONTH] == currentUtc[Calendar.MONTH]
            parsedResult[Calendar.DAY_OF_MONTH] == currentUtc[Calendar.DAY_OF_MONTH]
            parsedResult[Calendar.HOUR_OF_DAY] == currentUtc[Calendar.HOUR_OF_DAY] - 3
            parsedResult[Calendar.MINUTE] == currentUtc[Calendar.MINUTE]

    }

    void 'test offset is 180'() {
        given:
            Integer offset = 180

        when:
            String result = DateUtil.getCurrentTimeWithOffset(offset)
            Date parsedResult = Date.parse(dateFormat, result)

        then:
            result
            result != currentUtc.format(dateFormat)
            parsedResult[Calendar.YEAR] == currentUtc[Calendar.YEAR]
            parsedResult[Calendar.MONTH] == currentUtc[Calendar.MONTH]
            parsedResult[Calendar.DAY_OF_MONTH] == currentUtc[Calendar.DAY_OF_MONTH]
            parsedResult[Calendar.HOUR_OF_DAY] == currentUtc[Calendar.HOUR_OF_DAY] + 3
            parsedResult[Calendar.MINUTE] == currentUtc[Calendar.MINUTE]

    }

}
