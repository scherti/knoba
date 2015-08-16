'use strict';

angular.module('knobaApp').controller('UserDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'User',
        function($scope, $stateParams, $modalInstance, entity, User) {

        $scope.user = entity;
        $scope.load = function(id) {
            User.get({id : id}, function(result) {
                $scope.user = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('knobaApp:userUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.user.id != null) {
                User.update($scope.user, onSaveFinished);
            } else {
                User.save($scope.user, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
