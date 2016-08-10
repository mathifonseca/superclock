eventAllTestsStart = {
    if (getBinding().variables.containsKey("functionalTests")) {
        functionalTests << "functional"
    }
}

eventTestSuiteStart = { String type ->
    if (type == "functional") {
    	System.setProperty('grails.functional.test.serverPort', "$serverPort")
    	System.setProperty('grails.functional.test.serverContextPath', "$serverContextPath")
    }
}
