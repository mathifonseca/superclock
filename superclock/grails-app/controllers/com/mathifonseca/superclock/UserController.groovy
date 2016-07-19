package com.mathifonseca.superclock

import org.springframework.http.HttpStatus
import grails.rest.RestfulController

class UserController extends RestfulController {

    static responseFormats = ['json']

    UserController() {
        super(User)
    }

    def index() {

        List users = User.list()

        respond users

    }

    def save() {

        def body = request.JSON

        if (body.username && User.findByUsername(body.username)) {
            render status: HttpStatus.CONFLICT
            return
        }

        User user = new User(body)

        if (user.validate()) {
            user.save()
            new UserRole(user: user, role: Role.findByAuthority(Authority.USER.toString())).save()
            respond user, [status : HttpStatus.CREATED]
        } else {
            respond user.errors, [status : HttpStatus.UNPROCESSABLE_ENTITY]
        }

    }

    def update() {

        def body = request.JSON

        User user = User.get(params.id)

        if (!user) {
            render status: HttpStatus.NOT_FOUND
            return
        }

        bindData(user, body)

        if (user.validate()) {
            respond user.save()
        } else {
            respond user.errors, [status : HttpStatus.UNPROCESSABLE_ENTITY]
        }

    }

    def delete() {

        User user = User.get(params.id)

        if (!user) {
            render status: HttpStatus.NOT_FOUND
            return
        }

        UserRole.findAllByUser(user).each { it.delete() }

        user.delete()

        render status: HttpStatus.OK

    }

}
