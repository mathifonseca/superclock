var timezones = angular.module('timezones', []);

var loadTimezone = function($scope, $http, $routeParams) {
    $http.get('/superclock/api/timezones/' + $routeParams['timezoneId'], getHttpConfig())
        .success(function(data) {
            $scope.timezone = data;
            angular.copy($scope.timezone, $scope.copy);
        }
    );
};

var loadTimezones = function($scope, $http) {
    $http.get('/superclock/api/timezones', getHttpConfig())
        .success(function (data) {
            $scope.timezones = data;
        })
        .error(function (data) {
            $scope.timezones = data;
        }
    );
};

timezones.controller('listTimezonesController',
    function ($scope, $location, $http) {

        loadTimezones($scope, $http);

        $scope.createTimezone = function() {
            $location.path('/timezones/create/');
        };

        $scope.editTimezone = function(index) {
            $location.path('/timezones/' + $scope.timezones[index].id);
        };

        $scope.deleteTimezone = function(index) {
            var timezoneId = $scope.timezones[index].id;
            $http.delete('/superclock/api/timezones/' + timezoneId, getHttpConfig())
                .success(function(data) {
                    $location.path('/timezones');
                }
            );
        };
    }
);

timezones.controller('createTimezoneController',
    function ($scope, $location, $http) {

        $scope.showWeeks = false;
        $scope.toggleMin = function() {
            $scope.minDate = ( $scope.minDate ) ? null : new Date();
        };
        $scope.toggleMin();

        $scope.saveTimezone = function () {

            if ($scope.timezone) {
                $scope.timezone.username = $scope.currentUser;
            }

            $http.post('/superclock/api/timezones/', $scope.timezone, getHttpConfig())
                .success(function (data) {
                    $scope.timezone = '';
                    $location.path("/timezones")
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
            $location.path("/timezones");
        };
    }
);

timezones.controller('editTimezoneController',
    function ($scope, $location, $http, $routeParams) {

        loadTimezone($scope, $http, $routeParams);

        $scope.showWeeks = false;
        $scope.toggleMin = function() {
            $scope.minDate = ( $scope.minDate ) ? null : new Date();
        };
        $scope.toggleMin();

        $scope.saveTimezone = function() {

            if ($scope.timezone) {
                $scope.timezone.username = $scope.currentUser;
            }
            
            $http.put('/superclock/api/timezones/' + $scope.timezone.id, $scope.timezone, getHttpConfig())
                .success(function(data) {
                    $location.path('/timezones');
                })
                .error(function(data) {
                    var errors = {};
                    $(data.errors).each( function() {
                        errors[this.field] = this.message;
                    });
                    $scope.errors = errors;
                }
            );
        };

        $scope.cancel = function() {
            $location.path("/timezones")
        };
    }
);
