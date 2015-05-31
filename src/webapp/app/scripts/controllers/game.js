'use strict';

/**
 * @ngdoc function
 * @name passepartoutApp.controller:AboutCtrl
 * @description
 * # AboutCtrl
 * Controller of the passepartoutApp
 */
angular.module('passepartoutApp')
  .controller('GameCtrl', function ($scope, $http) {
  	$scope.questions = [];
  	$scope.currentQuestion = 0;
  	$scope.lives = 3;

    $scope.init = function() {
    	//$http(...)
    	$scope.questions = [
    		{
    			questionText: "What is the capital of Belgium",
    			options: [
    				"123",
    				"London",
    				"3456",
    				"errr"
    			],
    			answerIndex: 1,
    		},
    		{
    			questionText: "What is the capital of France",
    			options: [
    				"123",
    				"London",
    				"3456",
    				"errr"
    			],
    			answerIndex: 1,
    		},
    		{
    			questionText: "What is the capital of Germany",
    			options: [
    				"123",
    				"London",
    				"3456",
    				"errr"
    			],
    			answerIndex: 2,
    		},
    	]
    }

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

    $scope.init();
  });
