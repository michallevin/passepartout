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

	 $scope.username = {
		name: 'guest',
	    word: /^\s*\w*\s*$/
	 };

     $scope.startGame = function() {
    	console.log($scope.username.name);
     	$location.path('/game');
     };



  });
