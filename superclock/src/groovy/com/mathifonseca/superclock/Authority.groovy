package com.mathifonseca.superclock

enum Authority {
    USER('ROLE_USER'),
    ADMIN('ROLE_ADMIN')

    String value

    Authority(String value) {
        this.value = value
    }

    @Override
    public String toString() {
        return value
    }

}
