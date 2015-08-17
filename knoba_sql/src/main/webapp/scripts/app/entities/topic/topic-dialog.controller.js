'use strict';

angular.module('knobaApp').controller('TopicDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Topic', 'User',
        function($scope, $stateParams, $modalInstance, entity, Topic, User) {

        $scope.topic = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            Topic.get({id : id}, function(result) {
                $scope.topic = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('knobaApp:topicUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.topic.id != null) {
                Topic.update($scope.topic, onSaveFinished);
            } else {
                Topic.save($scope.topic, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
