
/**
 * @ngdoc overview
 * @name passepartoutApp
 * @description
 * # passepartoutApp
 *
 * Main module of the application.
 */
angular
  .module('passepartoutApp', [
    'passepartoutApp.manage',
    'passepartoutApp.questions',
    'passepartoutApp.user',
    'passepartoutApp.highscores',
    'ngRoute',
  ])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
      .when('/game', {
        templateUrl: 'views/game.html',
        controller: 'GameCtrl'
      })      
      .when('/highscores', {
          templateUrl: 'views/highscores.html',
          controller: 'ScoresCtrl'
        })
       .when('/admin', {
         templateUrl: 'views/admin.html',
         controller: 'AdminCtrl'
       }) 
        .when('/intro', {
         templateUrl: 'views/intro.html',
         controller: 'MainCtrl'
       }) 
      .otherwise({
        redirectTo: '/'
      });
  });
