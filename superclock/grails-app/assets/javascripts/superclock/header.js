var header = angular.module('header', []);

header.controller('headerController',
    function ($scope, $cookieStore) {

        $scope.isAuthenticated = $cookieStore.get("loggedIn") == true;
        $scope.isAdmin = $cookieStore.get("isAdmin") == true;
        $scope.currentUser = $cookieStore.get("currentUser");
        
        $scope.$on('updateHeader', function() {
            $scope.isAuthenticated = $cookieStore.get("loggedIn") == true;
            $scope.isAdmin = $cookieStore.get("isAdmin") == true;
            $scope.currentUser = $cookieStore.get("currentUser");
        });
    }
);

