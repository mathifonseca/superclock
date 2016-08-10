package com.mathifonseca.superclock

import com.mathifonseca.superclock.test.UnitTestUtil
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(TimezoneController)
@Mock([Timezone, User, UserRole])
class TimezoneControllerSpec extends Specification {

    void setup() {
        User.springSecurityService = [
            encodePassword : { String pass -> pass }
        ]
        controller.springSecurityService = [
            currentUser : UnitTestUtil.createUser()
        ]
    }

    void 'test index with empty list'() {
        when:
            controller.index()

        then:
            response.status == 200
            response.json == []
    }

    void 'test index with 10 items'() {
        given:
            10.times {
                UnitTestUtil.createTimezone()
            }

        when:
            controller.index()

        then:
            response.status == 200
            response.json
            response.json.size() == 10
            response.json[0].id
            response.json[0].name
            response.json[0].city
            response.json[0].gmtOffset
            response.json[0].user
            response.json[0].user.id
    }

    void 'test index multiple users'() {
        given:
            def tmz = UnitTestUtil.createTimezone()
            UnitTestUtil.createTimezone(UnitTestUtil.createUser('user2'))

        when:
            controller.index()

        then:
            response.status == 200
            response.json
            response.json.size() == 1
            response.json[0].id == tmz.id
    }

    void 'test index multiple users as admin'() {
        given:
            UnitTestUtil.createTimezone()
            UnitTestUtil.createTimezone(UnitTestUtil.createUser('user2'))
            controller.springSecurityService = [
                currentUser : UnitTestUtil.createAdmin()
            ]

        when:
            controller.index()

        then:
            response.status == 200
            response.json
            response.json.size() == 2
    }

    void 'test show no id'() {
        when:
            controller.show()

        then:
            response.status == 404
    }

    void 'test show invalid id'() {
        given:
            params.id = 9999

        when:
            controller.show()

        then:
            response.status == 404
    }

    void 'test show from another user'() {
        given:
            def tmz2 = UnitTestUtil.createTimezone(UnitTestUtil.createUser('user2'))
            params.id = tmz2.id

        when:
            controller.show()

        then:
            response.status == 403
    }

    void 'test show from another user as admin'() {
        given:
            def tmz2 = UnitTestUtil.createTimezone(UnitTestUtil.createUser('user2'))
            params.id = tmz2.id
            controller.springSecurityService = [
                currentUser : UnitTestUtil.createAdmin()
            ]

        when:
            controller.show()

        then:
            response.status == 200
            response.json
            response.json.id == tmz2.id
    }

    void 'test show valid id'() {
        given:
            def tmz = UnitTestUtil.createTimezone()
            params.id = tmz.id

        when:
            controller.show()

        then:
            response.status == 200
            response.json
            response.json.id == tmz.id
            response.json.name == tmz.name
            response.json.city == tmz.city
            response.json.gmtOffset == tmz.gmtOffset
            response.json.user
            response.json.user.id == tmz.user.id
    }

    void 'test save invalid params'() {
        given:
            def prevCount = Timezone.count()
            request.json = [
                name: name,
                city: city,
                gmtOffset: gmtOffset,
                username: username
            ]

        when:
            controller.save()

        then:
            prevCount == Timezone.count()
            response.status == 422
            response.json.errors
            response.json.errors.size() == 1
            response.json.errors[0].field == field

        where:
            name | city | gmtOffset | username | field
            null | 'city' | 100 | 'user' | 'name'
            'name' | null | 100 | 'user' | 'city'
            'name' | 'city' | null | 'user' | 'gmtOffset'
            'name' | 'city' | 99999 | 'user' | 'gmtOffset'
            'name' | 'city' | 100 | null | 'user'

    }

    void 'test save valid params'() {
        given:
            def prevCount = Timezone.count()
            def user = User.read(1)
            request.json = [
                name: 'name',
                city: 'city',
                gmtOffset: 180,
                username: user.username
            ]

        when:
            controller.save()

        then:
            response.status == 201
            response.json
            response.json.id
            response.json.name == 'name'
            response.json.city == 'city'
            response.json.gmtOffset == 180
            response.json.user
            response.json.user.id == user.id
            prevCount == Timezone.count() - 1

    }

    void 'test update no id'() {
        given:
            def prevCount = Timezone.count()
        when:
            controller.update()

        then:
            response.status == 404
            prevCount == Timezone.count()
    }

    void 'test update invalid id'() {
        given:
            def prevCount = Timezone.count()
            params.id = 9999

        when:
            controller.update()

        then:
            response.status == 404
            prevCount == Timezone.count()
    }

    void 'test update invalid params'() {
        given:
            def prevCount = Timezone.count()
            request.json = [
                name: name,
                city: city,
                gmtOffset: gmtOffset,
                username: username
            ]

        when:
            controller.save()

        then:
            prevCount == Timezone.count()
            response.status == 422
            response.json.errors
            response.json.errors.size() == 1
            response.json.errors[0].field == field

        where:
            name | city | gmtOffset | username | field
            '' | 'city' | 100 | 'user' | 'name'
            'name' | '' | 100 | 'user' | 'city'
            'name' | 'city' | 99999 | 'user' | 'gmtOffset'
            'name' | 'city' | 100 | '' | 'user'

    }

    void 'test update valid id'() {
        given:
            def tmz = UnitTestUtil.createTimezone()
            def prevCount = Timezone.count()
            params.id = tmz.id
            def body = [name:'name2']
            request.json = body

        when:
            controller.update()

        then:
            response.status == 200
            response.json
            response.json.id == tmz.id
            response.json.name == body.name
            response.json.city == tmz.city
            response.json.gmtOffset == tmz.gmtOffset
            response.json.user
            response.json.user.id == tmz.user.id
            prevCount == Timezone.count()
    }

    void 'test update from another user'() {
        given:
            def tmz2 = UnitTestUtil.createTimezone(UnitTestUtil.createUser('user2'))
            def prevCount = Timezone.count()
            params.id = tmz2.id
            request.json = [name:'name2']

        when:
            controller.update()

        then:
            response.status == 403
            prevCount == Timezone.count()
    }

    void 'test update from another user as admin'() {
        given:
            def tmz2 = UnitTestUtil.createTimezone(UnitTestUtil.createUser('user2'))
            params.id = tmz2.id
            request.json = [name:'name2']
            controller.springSecurityService = [
                currentUser : UnitTestUtil.createAdmin()
            ]

        when:
            controller.update()

        then:
            response.status == 200

    }

    void 'test delete no id'() {
        given:
            def prevCount = Timezone.count()
        when:
            controller.delete()

        then:
            response.status == 404
            prevCount == Timezone.count()
    }

    void 'test delete invalid id'() {
        given:
            def prevCount = Timezone.count()
            params.id = 9999

        when:
            controller.delete()

        then:
            response.status == 404
            prevCount == Timezone.count()
    }

    void 'test delete valid id'() {
        given:
            def tmz = UnitTestUtil.createTimezone()
            def prevCount = Timezone.count()
            params.id = tmz.id

        when:
            controller.delete()

        then:
            response.status == 200
            prevCount == Timezone.count() + 1
    }

    void 'test delete from another user'() {
        given:
            def tmz2 = UnitTestUtil.createTimezone(UnitTestUtil.createUser('user2'))
            def prevCount = Timezone.count()
            params.id = tmz2.id

        when:
            controller.delete()

        then:
            response.status == 403
            prevCount == Timezone.count()
    }

    void 'test delete from another user as admin'() {
        given:
            def tmz2 = UnitTestUtil.createTimezone(UnitTestUtil.createUser('user2'))
            def prevCount = Timezone.count()
            params.id = tmz2.id
            controller.springSecurityService = [
                    currentUser : UnitTestUtil.createAdmin()
            ]

        when:
            controller.delete()

        then:
            response.status == 200
            prevCount == Timezone.count() + 1
    }

}
