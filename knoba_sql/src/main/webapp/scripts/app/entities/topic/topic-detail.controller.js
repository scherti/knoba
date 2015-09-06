'use strict';

angular.module('knobaApp')
    .controller('TopicDetailController', function ($scope, $rootScope, $stateParams, entity, Topic, User) {
        $scope.topic = entity;
        $scope.load = function (id) {
            Topic.get({id: id}, function(result) {
                $scope.topic = result;
            });
        };
        $rootScope.$on('knobaApp:topicUpdate', function(event, result) {
            $scope.topic = result;
        });
    });
