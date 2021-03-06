'use strict';

/**
 * @ngdoc function
 * @name passepartoutApp.controller:AboutCtrl
 * @description
 * # AboutCtrl
 * Controller of the passepartoutApp
 */
angular.module('passepartoutApp')
.controller('GameCtrl', function ($scope, $Questions, $User, $location, $timeout, $Highscores) {

	$scope.currentQuestion = 0;
	$scope.lives = 3;
	$scope.score = 0;
	$scope.guessed = -1;
	$scope.gameOver = 0;
	$scope.loading = false;
	

	$scope.questions = $Questions.questions;
	
	if ($scope.questions.length == 0) {
		$location.path('/');
		return;
	}
	
	$scope.getAnswerClass = function(index) {
		if ($scope.guessed != -1 && index == $scope.questions[$scope.currentQuestion].answerIndex) {
			return "green"
		}
		else if ($scope.guessed == index) {
			return "red";
		}
		return "";
	}
	
	$scope.guessAnswer = function(index) {
		$scope.guessed = index;

		if (index == $scope.questions[$scope.currentQuestion].answerIndex) { //yes!
			$timeout(function() {
				$scope.currentQuestion += 1; 
				$scope.guessed =- 1;
				if ($scope.currentQuestion == $scope.questions.length) {
					$scope.gameOver = 1;
				}
			}, 500);
			$scope.score += $scope.questions[$scope.currentQuestion].score;
		}
		else { //no!
			
			$timeout(function(){
				$scope.lives -= 1;
				if ($scope.lives > 0) {
					$scope.currentQuestion += 1;
				}
				$scope.guessed =- 1;
				if ($scope.currentQuestion == $scope.questions.length) {
					$scope.gameOver = 1;
				}
			}, 500);
		}
		console.log($scope.currentQuestion);
		console.log($scope.questions.length);	
	}
	
	// for debugging
	$scope.advance = function() {
		$scope.currentQuestion += 1;
	}
	
	$scope.submitScore = function() {
		console.log($User.id)
		$User.setHighscore($scope.score)
		$Highscores.getHighscores(function() {
			$location.path('/highscores');
		})
		$scope.currentQuestion = 0;
		$scope.lives = 3;
		$scope.score = 0;
		$scope.gameOver = 0;

	}
	
	$scope.startOver= function() {
		$scope.loading = true;
		$Questions.getQuestions($User.id, function() {
			console.log("loaded questions");
			$scope.loading = false;
			$scope.questions = $Questions.questions;
			$scope.currentQuestion = 0;
			$scope.lives = 3;
			$scope.score = 0;
		});
		$scope.gameOver=0;
	}
	
	$scope.endGame = function() {
		$scope.currentQuestion = 0;
		$scope.lives = 3;
		$scope.score = 0;
		$scope.gameOver = 0;
		$location.path('/');
	}


});
