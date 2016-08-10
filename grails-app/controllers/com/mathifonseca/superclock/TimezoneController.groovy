package com.mathifonseca.superclock

import org.springframework.http.HttpStatus

import grails.rest.RestfulController

class TimezoneController extends RestfulController {

    static responseFormats = ['json']

    def springSecurityService

    TimezoneController() {
        super(Timezone)
    }

    def index() {

        User user = (User) springSecurityService.currentUser

        List timezones = user.isAdmin() ? Timezone.list() : (user.timezones?.toList() ?: [])

        respond timezones

    }

    def show(Timezone timezone) {

        if  (!timezone) {
            render status: HttpStatus.NOT_FOUND
            return
        }

        User user = (User) springSecurityService.currentUser

        if (!user.isAdmin() && timezone.user != user) {
            render status: HttpStatus.FORBIDDEN
            return
        }

        respond timezone

    }

    def save() {

        def body = request.JSON

        Timezone timezone = new Timezone(body)

        timezone.user = User.findByUsername(body.username)

        if (timezone.validate()) {
            respond timezone.save(), [status : HttpStatus.CREATED]
        } else {
            respond timezone.errors, [status : HttpStatus.UNPROCESSABLE_ENTITY]
        }

    }

    def update() {

        def body = request.JSON

        Timezone timezone = Timezone.get(params.id)

        if (!timezone) {
            render status: HttpStatus.NOT_FOUND
            return
        }

        User user = (User) springSecurityService.currentUser

        if (!user.isAdmin() && timezone.user != user) {
            render status: HttpStatus.FORBIDDEN
            return
        }

        bindData(timezone, body)

        if (body.username) {
            timezone.user = User.findByUsername(body.username)
        }

        if (timezone.validate()) {
            respond timezone.save()
        } else {
            respond timezone.errors, [status : HttpStatus.UNPROCESSABLE_ENTITY]
        }

    }

    def delete() {

        Timezone timezone = Timezone.get(params.id)

        if (!timezone) {
            render status: HttpStatus.NOT_FOUND
            return
        }

        User user = (User) springSecurityService.currentUser

        if (!user.isAdmin() && timezone.user != user) {
            render status: HttpStatus.FORBIDDEN
            return
        }

        timezone.delete()

        render status: HttpStatus.OK

    }


}
