grails.servlet.version = "3.0"
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.work.dir = "target/work"
grails.project.target.level = 1.7
grails.project.source.level = 1.7
grails.project.war.file = "target/${appName}.war"

grails.project.fork = [
    test: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon:true],
    run: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    war: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
]

grails.project.dependency.resolver = "maven"
grails.project.dependency.resolution = {
    inherits("global") {}
    log "error"
    checksums true
    legacyResolve false
    repositories {
        inherits true
        grailsPlugins()
        grailsHome()
        mavenLocal()
        grailsCentral()
        mavenCentral()
        mavenRepo 'https://oss.sonatype.org/content/repositories/snapshots'
        mavenRepo 'http://repo.spring.io/milestone/'
        mavenRepo 'http://repository.codehaus.org/'
    }
    dependencies {
    }
    plugins {
        build ':tomcat:7.0.54'
        compile ":asset-pipeline:1.9.6"
        compile ':cache:1.1.7'
        compile ':scaffolding:2.0.3'
        compile "org.grails.plugins:marshallers:0.7"
        compile ":spring-security-core:2.0-RC4"
        compile ":spring-security-rest:1.4.0.RC5", {
            excludes ('cors','spring-security-core')
        }
        compile "org.grails.plugins:rest:0.8"
        compile ":codenarc:0.25.2"
        runtime ':hibernate:3.6.10.16'
        provided ":version-update:1.6.0"
    }
}

codenarc {

    ruleSetFiles = "rulesets/basic.xml,rulesets/groovyism.xml,rulesets/exceptions.xml,rulesets/imports.xml,rulesets/grails.xml,rulesets/unused.xml,rulesets/logging.xml"
    maxPriority1Violations = 0
    systemExitOnBuildException = true

    processSrcGroovy = true
    processControllers = true
    processDomain = true
    processServices = true
    processTaglib = true
    processTestUnit = true
    processTestIntegration = true
    processViews = true
    extraIncludeDirs=['grails-app/filters']

    properties = {
        GrailsStatelessService.enabled = false
        CatchException.enabled = false
        Println.priority = 1
        SystemOutPrint.priority = 1
    }

    reports = {
        HtmlReport('html') {
            outputFile = 'CodeNarcReport.html'
            title = 'CodeNarcReport'
        }
    }

}