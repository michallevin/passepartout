'use strict';

/**
 * @ngdoc function
 * @name passepartoutApp.controller:AboutCtrl
 * @description
 * # AboutCtrl
 * Controller of the passepartoutApp
 */
angular.module('passepartoutApp')
.controller('AdminCrl', function ($scope, $http, $Admin) {

	$scope.isImporting = false;

		
	$scope.startImport = function() {
		$Admin.startImport(function() {
			$scope.isImporting = true;	
		});
	};

	$scope.cancelImport = function() {
		$Admin.cancelImport(function() {
			$scope.isImporting = false;
		});
	};

	$scope.getImportUpdate = function() {
		$Admin.getImportUpdate(function(isImporting) {
			$scope.isImporting = isImporting;
		});
	};


});
