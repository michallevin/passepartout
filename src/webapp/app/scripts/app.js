
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
    'passepartoutApp.questions',
    'passepartoutApp.user',
    'passepartoutApp.highscores',
    //'passepartoutApp.admin',
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch'
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
      .otherwise({
        redirectTo: '/'
      });
  });
