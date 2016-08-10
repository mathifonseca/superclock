package com.mathifonseca.superclock

import com.mathifonseca.superclock.test.UnitTestUtil
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(UserController)
@Mock([User, Role, UserRole])
class UserControllerSpec extends Specification {

    def user

    def setup() {
        new Role(authority: 'ROLE_USER').save()
        new Role(authority: 'ROLE_ADMIN').save()
        User.springSecurityService = [
            encodePassword : { String pass -> pass }
        ]
        user = UnitTestUtil.createUser()
    }

    void 'test index'() {
        given:
            10.times {
                UnitTestUtil.createUser()
            }

        when:
            controller.index()

        then:
            response.status == 200
            response.json
            response.json.size() == User.count()

    }

    void 'test show invalid id'() {
        given:
            params.id = 99999

        when:
            controller.show()

        then:
            response.status == 404
    }

    void 'test show'() {
        given:
            params.id = user.id

        when:
            controller.show()

        then:
            response.status == 200
            response.json
            response.json.id == user.id
            response.json.username == user.username
            response.json.enabled == user.enabled
            response.json.accountLocked == user.accountLocked
            response.json.accountExpired == user.accountExpired
            response.json.passwordExpired == user.passwordExpired

    }

    void 'test save invalid fields'() {
        given:
            def prevCount = User.count()
            request.json = [
                username: username,
                password: password
            ]

        when:
            controller.save()

        then:
            prevCount == User.count()
            response.status == 422
            response.json
            response.json.errors
            response.json.errors.size() == 1
            response.json.errors[0].field == field

        where:
            username | password | field
            null | 'name' | 'username'
            'name' | null | 'password'
    }

    void 'test save existing username'() {
        given:
            def prevCount = User.count()
            request.json = [
                username: 'user',
                password: 'user'
            ]

        when:
            controller.save()

        then:
            prevCount == User.count()
            response.status == 409

    }

    void 'test save'() {
        given:
            def prevCount = User.count()
            def body = [
                username: 'name',
                password: 'name'
            ]
            request.json = body

        when:
            controller.save()

        then:
            prevCount == User.count() - 1
            response.status == 201
            response.json
            response.json.id
            response.json.username == body.username
            response.json.enabled == true
            response.json.accountLocked == false
            response.json.accountExpired == false
            response.json.passwordExpired == false
    }

    void 'test update invalid fields'() {
        given:
            def prevCount = User.count()
            request.json = [
                username: username,
                password: password
            ]
            params.id = user.id

        when:
            controller.update()

        then:
            prevCount == User.count()
            response.status == 422
            response.json
            response.json.errors
            response.json.errors.size() == 1
            response.json.errors[0].field == field

        where:
            username | password | field
            '' | 'name' | 'username'
            'name' | '' | 'password'
    }

    void 'test update'() {
        given:
            def prevCount = User.count()
            def body = [
                username: 'name2',
                password: 'name'
            ]
            params.id = user.id
            request.json = body

        when:
            controller.update()

        then:
            prevCount == User.count()
            response.status == 200
            response.json
            response.json.id
            response.json.username == body.username
            response.json.enabled == true
            response.json.accountLocked == false
            response.json.accountExpired == false
            response.json.passwordExpired == false
    }

    void 'test delete invalid id'() {
        given:
            def prevCount = User.count()
            params.id = 99999

        when:
            controller.delete()

        then:
            prevCount == User.count()
            response.status == 404
    }

    void 'test delete'() {
        given:
            def prevCount = User.count()
            params.id = user.id

        when:
            controller.delete()

        then:
            prevCount == User.count() + 1
            response.status == 200
    }

}
