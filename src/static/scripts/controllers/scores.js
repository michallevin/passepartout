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
	$scope.scores = $Highscores.highscores;
	//$scope.scores = [{'name': 'Yotam', 'score': 230},{'name': 'Eden', 'score':450}]; 
});
