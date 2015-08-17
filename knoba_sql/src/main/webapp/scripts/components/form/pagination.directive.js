/* globals $ */
'use strict';

angular.module('knobaApp')
    .directive('knobaAppPagination', function() {
        return {
            templateUrl: 'scripts/components/form/pagination.html'
        };
    });
