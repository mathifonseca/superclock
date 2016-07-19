package com.mathifonseca.superclock.test

import static groovyx.net.http.ContentType.JSON

import groovyx.net.http.HttpResponseException
import groovyx.net.http.RESTClient

class FunctionalTestUtil {

    static RESTClient restClient = new RESTClient("http://localhost:8080/superclock/api/")

    static def get(uri, headers = [:], params = [:], asAdmin = false) {
        def resp
        try {
            resp = restClient.get(
                path: uri,
                query: params,
                headers: headers + [ 'X-Auth-Token' : getAuthToken(asAdmin) ]
            )
        } catch (HttpResponseException ex) {
            resp = [ status : ex.statusCode, data : ex.response.data ]
        }
        return resp
    }

    static def post(uri, body = [:], headers = [:], params = [:], asAdmin = false) {
        def resp
        try {
            resp = restClient.post(
                path: uri,
                query: params,
                contentType: JSON,
                headers: headers + [ 'X-Auth-Token' : getAuthToken(asAdmin) ],
                body: body
            )
        } catch (HttpResponseException ex) {
            resp = [ status : ex.statusCode, data : ex.response.data ]
        }
        return resp
    }

    static def put(uri, body = [:], headers = [:], params = [:], asAdmin = false) {
        def resp
        try {
            resp = restClient.put(
                path: uri,
                query: params,
                contentType: JSON,
                headers: headers + [ 'X-Auth-Token' : getAuthToken(asAdmin) ],
                body: body
            )
        } catch (HttpResponseException ex) {
            resp = [ status : ex.statusCode, data : ex.response.data ]
        }
        return resp
    }

    static def delete(uri, headers = [:], params = [:], asAdmin = false) {
        def resp
        try {
            resp = restClient.delete(
                path: uri,
                query: params,
                headers: headers + [ 'X-Auth-Token' : getAuthToken(asAdmin) ]
            )
        } catch (HttpResponseException ex) {
            resp = [ status : ex.statusCode, data : ex.response.data ]
        }
        return resp
    }

    static String getAuthToken(boolean admin = false) {
        def user = admin ? 'admin' : 'user'
        def resp = restClient.post(
            path: 'login',
            contentType: JSON,
            body: [username: user, password: user]
        )
        return resp.data.access_token
    }

    static def getTimezones(asAdmin = false) {
        def resp = get('timezones', [:], [:], asAdmin)
        return resp.data
    }

    static def getUsers() {
        def resp = get('users', [:], [:], true)
        return resp.data
    }

    static def createTimezone(String name, String city, Integer gmtOffset, String username) {
        def resp = post(
            'timezones',
            [ name : name, city: city, gmtOffset : gmtOffset, username: username ]
        )
        return resp.data
    }

}
