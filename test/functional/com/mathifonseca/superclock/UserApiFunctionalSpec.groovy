package com.mathifonseca.superclock

import com.mathifonseca.superclock.test.FunctionalTestUtil
import groovyx.net.http.HttpResponseException
import spock.lang.Specification

class UserApiFunctionalSpec extends Specification {

    private static final String URI = 'users'

    def 'get user list without token'() {
        when:
            FunctionalTestUtil.restClient.get(
                path: URI
            )
        then:
            def ex = thrown(HttpResponseException)
            ex.statusCode == 401
            ex.message == 'Unauthorized'
    }

    def 'get user list with user token'() {
        when:
            def resp = FunctionalTestUtil.get(URI)
        then:
            resp
            resp.status == 403
    }

    def 'get user list with valid token'() {
        when:
            def resp = FunctionalTestUtil.get(URI, [:], [:], true)

        then:
            resp.status == 200
            resp.data
            resp.data.size() == 3
            resp.data.find { it.username == 'user' }
            resp.data.find { it.username == 'user' }.enabled == true
            resp.data.find { it.username == 'user' }.password == null
            resp.data.find { it.username == 'user' }.passwordExpired == false
            resp.data.find { it.username == 'user' }.accountExpired == false
            resp.data.find { it.username == 'user' }.accountLocked == false
            resp.data.find { it.username == 'user' }.roles == 'ROLE_USER'
            resp.data.find { it.username == 'admin' }
            resp.data.find { it.username == 'admin' }.enabled == true
            resp.data.find { it.username == 'admin' }.password == null
            resp.data.find { it.username == 'admin' }.passwordExpired == false
            resp.data.find { it.username == 'admin' }.accountExpired == false
            resp.data.find { it.username == 'admin' }.accountLocked == false
            resp.data.find { it.username == 'admin' }.roles == 'ROLE_ADMIN'
    }

    def 'get user without token'() {
        when:
            FunctionalTestUtil.restClient.get(
                path: "$URI/1"
            )
        then:
            def ex = thrown(HttpResponseException)
            ex.statusCode == 401
            ex.message == 'Unauthorized'
    }

    def 'get user with user token'() {
        when:
            def resp = FunctionalTestUtil.get("$URI/1")
        then:
            resp
            resp.status == 403
    }

    def 'get user with valid token'() {
        when:
            def resp = FunctionalTestUtil.get("$URI/1", [:], [:], true)

        then:
            resp.status == 200
            resp.data
            resp.data.id == 1
            resp.data.username == 'user'
            resp.data.enabled == true
            resp.data.password == null
            resp.data.passwordExpired == false
            resp.data.accountExpired == false
            resp.data.accountLocked == false
            resp.data.roles == 'ROLE_USER'
    }

    def 'create user without token'() {
        when:
            FunctionalTestUtil.restClient.post(
                path: URI
            )
        then:
            def ex = thrown(HttpResponseException)
            ex.statusCode == 401
            ex.message == 'Unauthorized'
    }

    def 'create user with user token'() {
        when:
            def resp = FunctionalTestUtil.post(URI)
        then:
            resp
            resp.status == 403
    }

    def 'create user with invalid field'() {
        given:
            def prevCount = FunctionalTestUtil.getUsers().size()

        when:
            def resp = FunctionalTestUtil.post(
                URI,
                [ username : username, password: password ],
                [:], [:], true
            )
            def afterCount = FunctionalTestUtil.getUsers().size()

        then:
            resp
            resp.status == 422
            resp.data
            resp.data.errors
            resp.data.errors.size() == 1
            resp.data.errors[0].field == field
            prevCount == afterCount

        where:
            username | password | field
            'name1' | null | 'password'
            null | 'pass1' | 'username'
    }

    def 'create user with existing username'() {
        given:
            def prevCount = FunctionalTestUtil.getUsers().size()

        when:
            def resp = FunctionalTestUtil.post(
                URI,
                [ username : 'user', password: 'user' ],
                [:], [:], true
            )
            def afterCount = FunctionalTestUtil.getUsers().size()

        then:
            resp
            resp.status == 409
            prevCount == afterCount
    }

    def 'create user with valid data'() {
        given:
            def prevCount = FunctionalTestUtil.getUsers().size()
            def body = [ username : 'user3', password: 'user3' ]

        when:
            def resp = FunctionalTestUtil.post(
                URI,
                body,
                [:], [:], true
            )
            def afterCount = FunctionalTestUtil.getUsers().size()

        then:
            resp
            resp.status == 201
            resp.data
            !resp.data.errors
            resp.data.id
            resp.data.username == body.username
            !resp.data.password
            prevCount == afterCount - 1
    }

    def 'update user without token'() {
        when:
            FunctionalTestUtil.restClient.put(
                path: "$URI/1"
            )
        then:
            def ex = thrown(HttpResponseException)
            ex.statusCode == 401
            ex.message == 'Unauthorized'
    }

    def 'update user with user token'() {
        when:
            def resp = FunctionalTestUtil.put(URI)
        then:
            resp
            resp.status == 403
    }

    def 'update user with invalid field'() {
        when:
            def resp = FunctionalTestUtil.put(
                "$URI/1",
                [ username : username, password: password ],
                [:], [:], true
            )

        then:
            resp
            resp.status == 422
            resp.data
            resp.data.errors
            resp.data.errors.size() == 1
            resp.data.errors[0].field == field

        where:
            username | password | field
            'name1' | '' | 'password'
            '' | 'pass1' | 'username'
    }

    def 'update user with valid data'() {
        given:
            def body = [ username : 'user4', password: 'user4' ]

        when:
            def resp = FunctionalTestUtil.put(
                "$URI/1",
                body,
                [:], [:], true
            )

        then:
            resp
            resp.status == 200
            resp.data
            !resp.data.errors
            resp.data.id
            resp.data.username == body.username
            !resp.data.password
    }

    def 'delete user without token'() {
        when:
            FunctionalTestUtil.restClient.delete(
                path: "$URI/1"
            )
        then:
            def ex = thrown(HttpResponseException)
            ex.statusCode == 401
            ex.message == 'Unauthorized'
    }

    def 'delete user with user token'() {
        when:
            def resp = FunctionalTestUtil.delete("$URI/1")
        then:
            resp
            resp.status == 401
    }

    def 'delete user with invalid id'() {
        given:
            def prevCount = FunctionalTestUtil.getUsers().size()

        when:
            def resp = FunctionalTestUtil.delete(
                "$URI/999999",
                [:],
                [:],
                true
            )
            def afterCount = FunctionalTestUtil.getUsers().size()

        then:
            resp
            resp.status == 404
            prevCount == afterCount
    }

    def 'delete user with valid id'() {
        given:
            def prevCount = FunctionalTestUtil.getUsers().size()

        when:
            def resp = FunctionalTestUtil.delete(
                "$URI/1",
                [:],
                [:],
                true
            )
            def afterCount = FunctionalTestUtil.getUsers().size()

        then:
            resp
            resp.status == 200
            prevCount == afterCount + 1
    }

}
