package com.mathifonseca.superclock.test

import com.mathifonseca.superclock.Timezone
import com.mathifonseca.superclock.User

class UnitTestUtil {

    static User createUser(name = 'user') {
        User user = new User(username: name, password: name)
        user.save()
        return user
    }

    static User createAdmin(name = 'admin') {
        User user = new User(username: name, password: name)
        user.metaClass.isAdmin = { true }
        user.save()
        return user
    }

    static Timezone createTimezone(User user = null) {
        Timezone timezone = new Timezone(
            name: 'name',
            city: 'city',
            gmtOffset: 180,
            user: user ?: User.read(1)
        ).save()
        return timezone
    }

}
