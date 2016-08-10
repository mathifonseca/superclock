var account = angular.module('account', []);

account.controller('accountController',
    function ($rootScope, $location, $scope, $http, authService, $cookieStore) {

        $scope.register = function() {

            if (!$scope.authData) {
                return;
            }

            $http.post('api/signup', { username: $scope.authData.username, password: $scope.authData.password }, getAuthenticateHttpConfig())
                .success(function() {
                    login($http, authService, $cookieStore, $scope);
                })
                .error(function(data) {
                    $rootScope.$broadcast('event:auth-loginFailed', data);
                });

            $scope.accountForm.$setPristine();
        };

        $scope.login = function() {
            if (!$scope.authData) {
                return;
            }

            login($http, authService, $cookieStore, $scope);

            $scope.accountForm.$setPristine();
            $scope.authData.username = "";
            $scope.authData.password = "";
        };

        $scope.logout = function() {
            $http.post('api/logout', {}, getHttpConfig())
                .success(function() {
                    $cookieStore.put("loggedIn", false);
                    $cookieStore.put("currentUser", null);
                    $scope.$emit('updateHeader');
                    sessionStorage.clear();
                    $location.path("/");
                })
        }

    }
);

function login($http, authService, $cookieStore, $scope) {
    $http.post('api/login', { username: $scope.authData.username, password: $scope.authData.password }, getAuthenticateHttpConfig())
        .success(function(data) {
            $cookieStore.put("loggedIn", true);
            $cookieStore.put("currentUser", data.username);
            $cookieStore.put("isAdmin", data.roles.indexOf('ROLE_ADMIN') > -1);
            $scope.$emit('updateHeader');
            setLocalToken(data.access_token);
            authService.loginConfirmed({}, function(config) {
                var localToken = getLocalToken();
                var headerToken = config.headers["X-Auth-Token"];
                if(!headerToken || (headerToken != localToken)) {
                    config.headers["X-Auth-Token"] = getLocalToken();
                }
                return config;
            });
        })
        .error(function(data) {
            $rootScope.$broadcast('event:auth-loginFailed', data);
        });
}