'use strict';

angular.module('knobaApp')
    .factory('Topic', function ($resource, DateUtils) {
        return $resource('api/topics/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
