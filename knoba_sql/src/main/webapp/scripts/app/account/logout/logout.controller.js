'use strict';

angular.module('knobaApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
