package com.mathifonseca.superclock

class Timezone {

    String name
    String city
    Integer gmtOffset //in minutes

    String currentTime

    static transients = ['currentTime']

    static belongsTo = [ user : User ]

    static constraints = {
        name blank: false
        city blank: false
        gmtOffset min: -720, max: 840
    }

    static marshalling = {
        virtual {
            currentTime { value, json -> json.value(DateUtil.getCurrentTimeWithOffset(value.gmtOffset)) }
        }
    }

}