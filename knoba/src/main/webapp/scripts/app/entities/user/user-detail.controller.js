'use strict';

angular.module('knobaApp')
    .controller('UserDetailController', function ($scope, $rootScope, $stateParams, entity, User) {
        $scope.user = entity;
        $scope.load = function (id) {
            User.get({id: id}, function(result) {
                $scope.user = result;
            });
        };
        $rootScope.$on('knobaApp:userUpdate', function(event, result) {
            $scope.user = result;
        });
    });
