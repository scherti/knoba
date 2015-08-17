'use strict';

angular.module('knobaApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


