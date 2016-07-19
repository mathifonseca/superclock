package com.mathifonseca.superclock

class Timezone {

    String name
    String city
    Integer gmtOffset //in minutes

    static belongsTo = [ user : User ]

    static constraints = {
        name blank: false
        city blank: false
        gmtOffset min: -720, max: 840
    }

    static marshalling = {
        virtual {
            currentTime { value, json -> json.value(value.getCurrentTime()) }
        }
    }

    Date getCurrentTime() {

        Calendar calendar = new GregorianCalendar(timeZone: TimeZone.getTimeZone('UTC'))

        calendar.add(Calendar.MINUTE, gmtOffset)

        return calendar.time

    }

}