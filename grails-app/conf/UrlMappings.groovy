import static org.codehaus.groovy.grails.web.mapping.DefaultUrlMappingEvaluator.*

class UrlMappings {

	static mappings = {
        '/' (view:'/index')

        '/api/users' (resources: 'user', excludes: [ACTION_CREATE, ACTION_EDIT])
        '/api/timezones' (resources: 'timezone', excludes: [ACTION_CREATE, ACTION_EDIT])

        '/api/signup' (controller: 'user', action: 'save', method: 'POST')

        '403' (controller: 'error', action: 'forbidden')
	}

}