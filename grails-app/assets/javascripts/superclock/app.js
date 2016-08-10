var superclockApp = angular.module('superclockApp', [
    'http-auth-interceptor',
    'ngRoute',
    'ngCookies',
    'ui.bootstrap',
    'account',
    'timezones',
    'users',
    'header',
    'angularUtils.directives.dirPagination'
]);

superclockApp.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.
            when('/timezones', {
                templateUrl: 'assets/partials/timezone-list.html',
                controller: 'listTimezonesController'
            }).
            when('/timezones/create', {
                templateUrl: 'assets/partials/timezone-form.html',
                controller: 'createTimezoneController'
            }).
            when('/timezones/:timezoneId', {
                templateUrl: 'assets/partials/timezone-form.html',
                controller: 'editTimezoneController'
            }).
            when('/users', {
                templateUrl: 'assets/partials/user-list.html',
                controller: 'listUsersController'
            }).
            when('/users/create', {
                templateUrl: 'assets/partials/user-form.html',
                controller: 'createUserController'
            }).
            when('/users/:userId', {
                templateUrl: 'assets/partials/user-form.html',
                controller: 'editUserController'
            }).
            otherwise({
                redirectTo: function(routeParams, currentPath) {
                    if (currentPath === '/') {
                        return '/timezones';
                    }
                    return '/timezones';
                }
            });
    }]);

superclockApp.directive('confirmationNeeded', function () {
    return {
        priority: 1,
        terminal: true,
        link: function (scope, element, attrs) {
            var msg = attrs.confirmationNeeded || "Are you sure?";
            var clickAction = attrs.ngClick;
            element.bind('click', function () {
                if (window.confirm(msg)) {
                    scope.$eval(clickAction);
                }
            });
        }
    };
});

superclockApp.directive('showLogin', function() {
    return {
        restrict: 'C',
        link: function(scope, element) {
            var login = element.find('#login-holder');
            var loginError = element.find('#login-error');
            var main = element.find('#content');
            var username = element.find('#username');
            var password = element.find('#password');

            login.hide();
            loginError.hide();

            scope.$on('event:auth-loginRequired', function() {
                main.hide();
                username.val('');
                password.val('');
                login.show();
            });
            scope.$on('event:auth-loginFailed', function() {
                username.val('');
                password.val('');
                loginError.show();
            });
            scope.$on('event:auth-loginConfirmed', function() {
                main.show();
                login.hide();
                username.val('');
                password.val('');
            });
        }
    }
});

function getLocalToken() {
   return sessionStorage["authToken"];
}

function setLocalToken(value) {
    sessionStorage["authToken"] = value;
}

function getHttpConfig() {
    return {
        headers: {
            'X-Auth-Token': getLocalToken()
        }
    };
}

function getAuthenticateHttpConfig() {
    return {
        ignoreAuthModule: true
    };
}
