'use strict';

angular.module('knobaApp')
    .controller('TopicController', function ($scope, Topic) {
        $scope.topics = [];
        $scope.loadAll = function() {
            Topic.query(function(result) {
               $scope.topics = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Topic.get({id: id}, function(result) {
                $scope.topic = result;
                $('#deleteTopicConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Topic.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTopicConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.topic = {name: null, id: null};
        };
    });
