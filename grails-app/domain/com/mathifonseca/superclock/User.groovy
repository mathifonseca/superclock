package com.mathifonseca.superclock

class User {

    static transient springSecurityService

    String username
    String password
    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired

    static hasMany = [ timezones : Timezone ]

    static transients = ['springSecurityService']

    static constraints = {
        username blank: false, unique: true
        password blank: false
    }

    static mapping = {
        password column: '`password`'
        timezones cascade: 'all-delete-orphan'
    }

    static marshalling = {
        shouldOutputClass false
        ignore 'password'
        virtual {
            roles { value, json -> json.value(value.getAuthorities().authority.join(', ')) }
        }
    }

    boolean isAdmin() {
        return getAuthorities().any { it.authority == Authority.ADMIN.toString() }
    }

    Set<Role> getAuthorities() {
        UserRole.findAllByUser(this).collect { it.role } as Set
    }

    def beforeInsert() {
        encodePassword()
    }

    def beforeUpdate() {
        if (isDirty('password')) {
            encodePassword()
        }
    }

    protected void encodePassword() {
        password = springSecurityService.encodePassword(password)
    }

}
