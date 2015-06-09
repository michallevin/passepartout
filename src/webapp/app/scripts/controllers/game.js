'use strict';

/**
 * @ngdoc function
 * @name passepartoutApp.controller:AboutCtrl
 * @description
 * # AboutCtrl
 * Controller of the passepartoutApp
 */
angular.module('passepartoutApp')
.controller('GameCtrl', function ($scope, $http, $Questions) {

	$scope.currentQuestion = 0;
	$scope.lives = 3;


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
		}
		else {
			alert("NO!");
			$scope.currentQuestion += 1;
			$scope.lives -= 1;
		}

	}


});
