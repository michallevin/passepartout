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
		
		if (!$scope.username.name) {
			alert("You must choose a username!")
			return;
		}
		console.log($scope.username.name);
		$User.name=$scope.username.name;
		$scope.loading = true;
		$User.getUserId(function() {
			console.log("loaded questions");
			$scope.loading = false;
			$location.path('/intro');
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
	$scope.gotoIntro = function() {
		console.log($scope.username.name);
//		$User.name=$scope.username.name;
//		$scope.loading = true;
//		$User.getHighScores(function() {
//			console.log("loaded questions");
//			$scope.loading = false;
			$location.path('/intro');
//		});

	};

});
