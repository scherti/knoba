/* globals $ */
'use strict';

angular.module('knobaApp')
    .directive('knobaAppPager', function() {
        return {
            templateUrl: 'scripts/components/form/pager.html'
        };
    });
