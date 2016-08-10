grails.config.locations = [ Log4jConfig ]

grails.project.groupId = 'com.mathifonseca.superclock'

grails.mime.disable.accept.header.userAgents = ['Gecko', 'WebKit', 'Presto', 'Trident']
grails.mime.types = [
    all: '*/*',
    atom: 'application/atom+xml',
    css: 'text/css',
    csv: 'text/csv',
    form: 'application/x-www-form-urlencoded',
    html: ['text/html', 'application/xhtml+xml'],
    js: 'text/javascript',
    json: ['application/json', 'text/json'],
    multipartForm: 'multipart/form-data',
    rss: 'application/rss+xml',
    text: 'text/plain',
    hal: ['application/hal+json', 'application/hal+xml'],
    xml: ['text/xml', 'application/xml']
]

grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']
grails.resources.adhoc.excludes = ['/WEB-INF/**']
grails.views.default.codec = "html"
grails.controllers.defaultScope = 'singleton'

grails {
    views {
        gsp {
            encoding = 'UTF-8'
            htmlcodec = 'xml'
            codecs {
                expression = 'html'
                scriptlet = 'html'
                taglib = 'none'
                staticparts = 'none'
            }
        }
    }
}

grails.converters.encoding = "UTF-8"
grails.scaffolding.templates.domainSuffix = 'Instance'
grails.json.legacy.builder = false
grails.enable.native2ascii = true
grails.spring.bean.packages = []
grails.web.disable.multipart = false
grails.exceptionresolver.params.exclude = ['password']
grails.hibernate.cache.queries = false

grails.reload.enabled = true
grails.gsp.enable.reload = true
grails.gorm.failOnError = true
grails.databinding.dateFormats = ["yyyy-MM-dd'T'HH:mm:ss'Z'"]
grails.databinding.convertEmptyStringsToNull = false

grails.app.context = '/superclock'

environments {
    development {
        grails.logging.jul.usebridge = true
    }
    production {
        grails.logging.jul.usebridge = false
    }
}

grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.mathifonseca.superclock.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.mathifonseca.superclock.UserRole'
grails.plugin.springsecurity.authority.className = 'com.mathifonseca.superclock.Role'
grails.plugin.springsecurity.securityConfigType = 'InterceptUrlMap'
grails.plugin.springsecurity.interceptUrlMap = [
    '/':                    ['permitAll'],
    '/index':               ['permitAll'],
    '/index.gsp':           ['permitAll'],
    '/assets/**':           ['permitAll'],
    '/partials/**':         ['permitAll'],
    '/api/signup':          ['permitAll'],
    '/api/timezones/**':    ['ROLE_USER','ROLE_ADMIN'],
    '/api/users/**':        ['ROLE_ADMIN'],
    '/**':                  ['isFullyAuthenticated()']
]
grails.plugin.springsecurity.filterChain.chainMap = [
    '/api/signup': 'JOINED_FILTERS,-restTokenValidationFilter,-restExceptionTranslationFilter',
    '/api/**': 'JOINED_FILTERS,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter',
    '/data/**': 'JOINED_FILTERS,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter',
    '/**': 'JOINED_FILTERS,-restTokenValidationFilter,-restExceptionTranslationFilter',
]

grails.plugin.springsecurity.rememberMe.persistent = false
grails.plugin.springsecurity.rest.login.useJsonCredentials = true
grails.plugin.springsecurity.rest.login.failureStatusCode = 401
grails.plugin.springsecurity.rest.token.storage.useGorm = true
grails.plugin.springsecurity.rest.token.storage.gorm.tokenDomainClassName = 'com.mathifonseca.superclock.Token'
grails.plugin.springsecurity.rest.token.storage.gorm.tokenValuePropertyName = 'token'
grails.plugin.springsecurity.rest.token.storage.gorm.usernamePropertyName = 'username'

grails.plugin.springsecurity.rest.token.validation.headerName = 'X-Auth-Token'
grails.plugin.springsecurity.rest.token.validation.useBearerToken = false