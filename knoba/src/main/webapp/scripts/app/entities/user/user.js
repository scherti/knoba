'use strict';

angular.module('knobaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('user', {
                parent: 'entity',
                url: '/users',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'knobaApp.user.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/user/users.html',
                        controller: 'UserController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('user');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('user.detail', {
                parent: 'entity',
                url: '/user/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'knobaApp.user.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/user/user-detail.html',
                        controller: 'UserDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('user');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'User', function($stateParams, User) {
                        return User.get({id : $stateParams.id});
                    }]
                }
            })
            .state('user.new', {
                parent: 'user',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/user/user-dialog.html',
                        controller: 'UserDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('user', null, { reload: true });
                    }, function() {
                        $state.go('user');
                    })
                }]
            })
            .state('user.edit', {
                parent: 'user',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/user/user-dialog.html',
                        controller: 'UserDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['User', function(User) {
                                return User.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('user', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
