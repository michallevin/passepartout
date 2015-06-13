'use strict';

/**
 * @ngdoc function
 * @name passepartoutApp.controller:AboutCtrl
 * @description
 * # AboutCtrl
 * Controller of the passepartoutApp
 */
angular.module('passepartoutApp')
.controller('GameCtrl', function ($scope, $http, $Questions, $User, $location) {

	$scope.currentQuestion = 0;
	$scope.lives = 0;
	$scope.score=0;


//	$scope.init = function() {
//		//$http(localhost:8080/rest/question)
//		$Questions.getQuestions();
//		$scope.questions = $Questions.questions;
//
//	}
	
	$scope.questions=$Questions.questions
	
	$scope.guessAnswer = function(index) {
		//console.log(index);
		if (index == $scope.questions[$scope.currentQuestion].answerIndex) {
			alert("Yes!");
			$scope.currentQuestion += 1;
			$scope.score+=100;
		}
		else {
			alert("NO!");
			$scope.lives -= 1;
			if ($scope.lives>0) {
				$scope.currentQuestion += 1; }
					}
	}
	
	
	$scope.submitScore = function() {
		$User.setHighscore($scope.score)
		//go to high score page
	}
	
	$scope.startOver= function() {
		$scope.loading = true;
		$User.getUserId(function() {
			console.log("loaded questions");
			$scope.loading = false;
			$scope.questions=$Questions.questions;
			$scope.currentQuestion = 0;
			$scope.lives = 3;
		});
	}
	
	$scope.endGame = function() {
		$scope.currentQuestion = 0;
		$scope.lives = 3;
		$scope.score=0;
		$location.path('/');
	}


});
