'use strict';

angular.module('knobaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('topic', {
                parent: 'entity',
                url: '/topics',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'knobaApp.topic.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/topic/topics.html',
                        controller: 'TopicController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('topic');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('topic.detail', {
                parent: 'entity',
                url: '/topic/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'knobaApp.topic.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/topic/topic-detail.html',
                        controller: 'TopicDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('topic');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Topic', function($stateParams, Topic) {
                        return Topic.get({id : $stateParams.id});
                    }]
                }
            })
            .state('topic.new', {
                parent: 'topic',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/topic/topic-dialog.html',
                        controller: 'TopicDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {name: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('topic', null, { reload: true });
                    }, function() {
                        $state.go('topic');
                    })
                }]
            })
            .state('topic.edit', {
                parent: 'topic',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/topic/topic-dialog.html',
                        controller: 'TopicDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Topic', function(Topic) {
                                return Topic.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('topic', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
