'use strict';

/**
 * @ngdoc function
 * @name passepartoutApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the passepartoutApp
 */
angular.module('passepartoutApp')
.controller('ScoresCtrl', function ($scope, $location, $User, $Highscores) {
//	$scope.scores = $Highscores.highscores;
	$scope.scores = {'Yotam': 230,'Eden':450}; 


});
