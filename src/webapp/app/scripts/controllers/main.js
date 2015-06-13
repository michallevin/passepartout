'use strict';

/**
 * @ngdoc function
 * @name passepartoutApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the passepartoutApp
 */
angular.module('passepartoutApp')
.controller('MainCtrl', function ($scope, $location, $User) {

	$scope.username = {
			name: 'guest',
			word: /^\s*\w*\s*$/
	};

	$scope.startGame = function() {
		console.log($scope.username.name);
		$User.name=$scope.username.name;
		$scope.loading = true;
		$User.getUserId(function() {
			console.log("loaded questions");
			$scope.loading = false;
			$location.path('/game');
		});

	};
	$scope.gotoHighScores = function() {
		console.log($scope.username.name);
//		$User.name=$scope.username.name;
//		$scope.loading = true;
//		$User.getHighScores(function() {
//			console.log("loaded questions");
//			$scope.loading = false;
			$location.path('/highscores');
//		});

	};
	$scope.gotoAdmin = function() {
		console.log($scope.username.name);
//		$User.name=$scope.username.name;
//		$scope.loading = true;
//		$User.getHighScores(function() {
//			console.log("loaded questions");
//			$scope.loading = false;
			$location.path('/admin');
//		});

	};


});
