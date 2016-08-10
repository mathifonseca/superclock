import org.apache.log4j.PatternLayout;

log4j =
{
    environments
    {
        development
        {
            appenders
            {
                console     name            : "stdout",
                            layout          : pattern( conversionPattern : '%d{yyyy-MM-dd HH:mm:ss} %-5p [%-18c{1}] %m%n' );
            };
        
            error  'org.codehaus.groovy.grails.web.servlet',        // controllers
                   'org.codehaus.groovy.grails.web.pages',          // GSP
                   'org.codehaus.groovy.grails.web.sitemesh',       // layouts
                   'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
                   'org.codehaus.groovy.grails.web.mapping',        // URL mapping
                   'org.codehaus.groovy.grails.commons',            // core / classloading
                   'org.codehaus.groovy.grails.plugins',            // plugins
                   'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration
                   'org.springframework',
                   'org.hibernate',
                   'net.sf.ehcache.hibernate';

            info 'grails.plugin.springsecurity.web.filter.DebugFilter'
            debug 'org.springframework.security'

            root
            {
                info 'stdout';
            };
        }
        test
        {
            appenders
            {
                console     name            : "stdout",
                            layout          : pattern( conversionPattern : '%d{yyyy-MM-dd HH:mm:ss} %-5p [%-18c{1}] %m%n' );
            };

            error  'org.codehaus.groovy.grails.web.servlet',        // controllers
                    'org.codehaus.groovy.grails.web.pages',          // GSP
                    'org.codehaus.groovy.grails.web.sitemesh',       // layouts
                    'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
                    'org.codehaus.groovy.grails.web.mapping',        // URL mapping
                    'org.codehaus.groovy.grails.commons',            // core / classloading
                    'org.codehaus.groovy.grails.plugins',            // plugins
                    'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration
                    'org.springframework',
                    'org.hibernate',
                    'net.sf.ehcache.hibernate';

            root
            {
                info 'stdout';
            };
        }
        production
        {
            appenders
            {
                console     name            : "stdout",
                            layout          : pattern( conversionPattern : '%d{yyyy-MM-dd HH:mm:ss} %-5p [%-18c{1}] %m%n' );
            };

            error  'org.codehaus.groovy.grails.web.servlet',        // controllers
                    'org.codehaus.groovy.grails.web.pages',          // GSP
                    'org.codehaus.groovy.grails.web.sitemesh',       // layouts
                    'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
                    'org.codehaus.groovy.grails.web.mapping',        // URL mapping
                    'org.codehaus.groovy.grails.commons',            // core / classloading
                    'org.codehaus.groovy.grails.plugins',            // plugins
                    'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration
                    'org.springframework',
                    'org.hibernate',
                    'net.sf.ehcache.hibernate';

            root
            {
                info 'stdout';
            };
        }

    }
}