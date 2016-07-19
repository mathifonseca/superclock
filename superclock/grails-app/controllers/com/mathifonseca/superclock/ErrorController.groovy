package com.mathifonseca.superclock

class ErrorController {

    def forbidden() {
        render (contentType: "text/json") { error: [code: 403, msg: "Access Denied."] }
    }

}
