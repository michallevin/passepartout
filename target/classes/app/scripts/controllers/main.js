'use strict';

/**
 * @ngdoc function
 * @name passepartoutApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the passepartoutApp
 */
angular.module('passepartoutApp')
  .controller('MainCtrl', function ($scope, $location) {


     $scope.startGame = function() {
     	$location.path('/game');
     };

  });
