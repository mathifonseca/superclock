var users = angular.module('users', []);

var loadUser = function($scope, $http, $routeParams) {
    $http.get('/superclock/api/users/' + $routeParams['userId'], getHttpConfig())
        .success(function(data) {
            $scope.user = data;
            angular.copy($scope.user, $scope.copy);
        }
    );
};

var loadUsers = function($scope, $http) {
    $http.get('/superclock/api/users', getHttpConfig())
        .success(function (data) {
            $scope.users = data;
        })
        .error(function (data) {
            $scope.users = data;
        }
    );
};

users.controller('listUsersController',
    function ($scope, $location, $http) {

        if (!$scope.isAdmin) {
            $location.path('/timezones');
        }

        loadUsers($scope, $http);

        $scope.createUser = function() {
            $location.path('/users/create/');
        };

        $scope.editUser = function(index) {
            $location.path('/users/' + $scope.users[index].id);
        };

        $scope.deleteUser = function(index) {
            var userId = $scope.users[index].id;
            $http.delete('/superclock/api/users/' + userId, getHttpConfig())
                .success(function(data) {
                    $location.path('/users');
                }
            );
        };
    }
);

users.controller('createUserController',
    function ($scope, $location, $http) {

        if (!$scope.isAdmin) {
            $location.path('/timezones');
        }

        $scope.saveUser = function () {

            $http.post('/superclock/api/users/', $scope.user, getHttpConfig())
                .success(function (data) {
                    $scope.user = '';
                    $location.path("/users")
                })
                .error(function (data) {
                    var errors = {};
                    $(data.errors).each( function() {
                        errors[this.field] = this.message;
                    });
                    $scope.errors = errors;
                }
            );
        };

        $scope.cancel = function() {
            $location.path("/users");
        };
    }
);

users.controller('editUserController',
    function ($scope, $location, $http, $routeParams) {

        if (!$scope.isAdmin) {
            $location.path('/timezones');
        }

        loadUser($scope, $http, $routeParams);

        $scope.saveUser = function() {
            $http.put('/superclock/api/users/' + $scope.user.id, $scope.user, getHttpConfig())
                .success(function(data) {
                    $location.path('/users');
                });
        };

        $scope.cancel = function() {
            $location.path("/users")
        };
    }
);
