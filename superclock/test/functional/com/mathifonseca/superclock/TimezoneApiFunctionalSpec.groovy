package com.mathifonseca.superclock

import groovyx.net.http.HttpResponseException
import spock.lang.Specification
import com.mathifonseca.superclock.test.FunctionalTestUtil
import spock.lang.Unroll

class TimezoneApiFunctionalSpec extends Specification {

    private static final String URI = 'timezones'

    def 'get timezone list without token'() {
        when:
            FunctionalTestUtil.restClient.get(
                path: URI
            )
        then:
            def ex = thrown(HttpResponseException)
            ex.statusCode == 401
            ex.message == 'Unauthorized'
    }

    def 'get timezone list with invalid token'() {
        when:
            FunctionalTestUtil.restClient.get(
                path: URI,
                headers: ['X-Auth-Token' : 'invalid']
            )

        then:
            def ex = thrown(HttpResponseException)
            ex.statusCode == 401
            ex.message == 'Unauthorized'
    }

    def 'get timezone list with valid token, empty list'() {
        when:
            def resp = FunctionalTestUtil.get(URI)

        then:
            resp.status == 200
            resp.data == []
    }

    def 'get timezone list with valid token one item'() {
        given:
            def tmz = FunctionalTestUtil.createTimezone('GMT', 'London', 0, 'user')

        when:
            def resp = FunctionalTestUtil.get(URI)

        then:
            resp.status == 200
            resp.data
            resp.data.find { it.id == tmz.id }
            resp.data.find { it.id == tmz.id }.name == tmz.name
            resp.data.find { it.id == tmz.id }.city == tmz.city
            resp.data.find { it.id == tmz.id }.gmtOffset == tmz.gmtOffset
    }

    def 'get timezone list different users'() {
        given:
            FunctionalTestUtil.createTimezone('GMT', 'London', 0, 'user')
            FunctionalTestUtil.createTimezone('UYT', 'Montevideo', -180, 'user2')
            FunctionalTestUtil.createTimezone('ART', 'Buenos Aires', -180, 'user2')

        when:
            def resp = FunctionalTestUtil.get(URI)

        then:
            resp.status == 200
            resp.data
            resp.data.every { it.user.id == 1 }
    }

    def 'get timezone list admin user'() {
        given:
            FunctionalTestUtil.createTimezone('GMT', 'London', 0, 'user')
            FunctionalTestUtil.createTimezone('UYT', 'Montevideo', -180, 'user2')
            FunctionalTestUtil.createTimezone('ART', 'Buenos Aires', -180, 'user2')

        when:
            def resp = FunctionalTestUtil.get(URI, [:], [:], true)

        then:
            resp.status == 200
            resp.data
            (resp.data.user.id as Set).size() > 1
    }

    def 'create timezone without token'() {
        when:
            FunctionalTestUtil.restClient.post(
                path: URI
            )
        then:
            def ex = thrown(HttpResponseException)
            ex.statusCode == 401
            ex.message == 'Unauthorized'
    }

    def 'create timezone with invalid field'() {
        given:
            def prevCount = FunctionalTestUtil.getTimezones(true).size()

        when:
            def resp = FunctionalTestUtil.post(
                URI,
                [ name : name, city: city, gmtOffset : gmtOffset, username: username ]
            )
            def afterCount = FunctionalTestUtil.getTimezones(true).size()

        then:
            resp
            resp.status == 422
            resp.data
            resp.data.errors
            resp.data.errors.size() == 1
            resp.data.errors[0].field == field
            prevCount == afterCount

        where:
            name | city | gmtOffset | username | field
            'name1' | 'city1' | 180 | null | 'user'
            'name1' | 'city1' | null | 'user' | 'gmtOffset'
            'name1' | null | 180 | 'user' | 'city'
            null | 'city1' | 180 | 'user' | 'name'
            'name1' | 'city1' | 1800 | 'user' | 'gmtOffset'

    }

    def 'create timezone valid'() {
        given:
            def prevCount = FunctionalTestUtil.getTimezones(true).size()
            def body = [ name : 'UYT', city: 'Montevideo', gmtOffset : -180, username: 'user' ]

        when:
            def resp = FunctionalTestUtil.post(
                URI,
                body
            )
            def afterCount = FunctionalTestUtil.getTimezones(true).size()

        then:
            resp
            resp.status == 201
            resp.data
            !resp.data.errors
            resp.data.id
            resp.data.name == body.name
            resp.data.city == body.city
            resp.data.gmtOffset == body.gmtOffset
            prevCount == afterCount - 1

    }

    def 'update timezone without token'() {
        when:
            FunctionalTestUtil.restClient.put(
                path: "$URI/1"
            )
        then:
            def ex = thrown(HttpResponseException)
            ex.statusCode == 401
            ex.message == 'Unauthorized'
    }

    @Unroll
    def 'update timezone with missing field'() {
        given:
            def tmz = FunctionalTestUtil.createTimezone('GMT', 'London', 0, 'user')

        when:
            def resp = FunctionalTestUtil.put(
                "$URI/$tmz.id",
                [ name : name, city: city, gmtOffset : gmtOffset, username: username ]
            )

        then:
            resp
            resp.status == 422
            resp.data
            resp.data.errors
            resp.data.errors.size() == 1
            resp.data.errors[0].field == field

        where:
            name | city | gmtOffset | username | field
            'name1' | 'city1' | 180 | 'invaliduser' | 'user'
            'name1' | 'city1' | '' | 'user' | 'gmtOffset'
            'name1' | '' | 180 | 'user' | 'city'
            '' | 'city1' | 180 | 'user' | 'name'
            'name1' | 'city1' | 1800 | 'user' | 'gmtOffset'

    }

    def 'update timezone valid'() {
        given:
            def tmz = FunctionalTestUtil.createTimezone('GMT', 'London', 0, 'user')

            def body = [ name : 'UYT', city: 'Montevideo', gmtOffset : -180, username: 'user' ]

        when:
            def resp = FunctionalTestUtil.put(
                "$URI/$tmz.id",
                body
            )

        then:
            resp
            resp.status == 200
            resp.data
            !resp.data.errors
            resp.data.id
            resp.data.name == body.name
            resp.data.city == body.city
            resp.data.gmtOffset == body.gmtOffset

    }

    def 'update timezone from another user'() {
        given:
            def tmz = FunctionalTestUtil.createTimezone('GMT', 'London', 0, 'user2')

            def body = [ name : 'UYT', city: 'Montevideo', gmtOffset : -180, username: 'user' ]

        when:
            def resp = FunctionalTestUtil.put(
                "$URI/$tmz.id",
                body
            )

        then:
            resp
            resp.status == 403
            !resp.data

    }

    def 'update timezone as admin'() {
        given:
            def tmz = FunctionalTestUtil.createTimezone('GMT', 'London', 0, 'user2')

            def body = [ name : 'UYT', city: 'Montevideo', gmtOffset : -180, username: 'user' ]

        when:
            def resp = FunctionalTestUtil.put(
                "$URI/$tmz.id",
                body,
                [:],
                [:],
                true
            )

        then:
            resp
            resp.status == 200
            resp.data
            !resp.data.errors
            resp.data.id
            resp.data.name == body.name
            resp.data.city == body.city
            resp.data.gmtOffset == body.gmtOffset

    }

    def 'delete timezone without token'() {
        when:
            FunctionalTestUtil.restClient.delete(
                path: "$URI/1"
            )
        then:
            def ex = thrown(HttpResponseException)
            ex.statusCode == 401
            ex.message == 'Unauthorized'
    }

    def 'delete timezone invalid id'() {
        given:
            def prevCount = FunctionalTestUtil.getTimezones(true).size()

        when:
            def resp = FunctionalTestUtil.delete(
                "$URI/99999"
            )
            def afterCount = FunctionalTestUtil.getTimezones(true).size()
        then:
            resp
            resp.status == 404
            !resp.data
            prevCount == afterCount

    }

    def 'delete timezone from another user'() {
        given:
            def tmz = FunctionalTestUtil.createTimezone('GMT', 'London', 0, 'user2')
            def prevCount = FunctionalTestUtil.getTimezones(true).size()

        when:
            def resp = FunctionalTestUtil.delete(
                "$URI/$tmz.id"
            )
            def afterCount = FunctionalTestUtil.getTimezones(true).size()
        then:
            resp
            resp.status == 403
            !resp.data
            prevCount == afterCount

    }

    def 'delete timezone as admin'() {
        given:
            def tmz = FunctionalTestUtil.createTimezone('GMT', 'London', 0, 'user2')
            def prevCount = FunctionalTestUtil.getTimezones(true).size()

        when:
            def resp = FunctionalTestUtil.delete(
                "$URI/$tmz.id",
                [:],
                [:],
                true
            )
            def afterCount = FunctionalTestUtil.getTimezones(true).size()
        then:
            resp
            resp.status == 200
            !resp.data
            prevCount == afterCount + 1

    }

    def 'delete timezone valid'() {
        given:
            def tmz = FunctionalTestUtil.createTimezone('GMT', 'London', 0, 'user')
            def prevCount = FunctionalTestUtil.getTimezones(true).size()

        when:
            def resp = FunctionalTestUtil.delete(
                "$URI/$tmz.id"
            )
            def afterCount = FunctionalTestUtil.getTimezones(true).size()

        then:
            resp
            resp.status == 200
            !resp.data
            prevCount == afterCount + 1

    }

}
