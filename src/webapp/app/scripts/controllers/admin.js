'use strict';

/**
 * @ngdoc function
 * @name passepartoutApp.controller:AboutCtrl
 * @description
 * # AboutCtrl
 * Controller of the passepartoutApp
 */
angular.module('passepartoutApp')
.controller('AdminCrl', function ($scope, $http, $AdminService) {

	$scope.isImporting = false;

		
	$scope.startImport = function() {
		$AdminService.startImport(function() {
			$scope.isImporting = true;
		});
	}

	$scope.cancelImport = function() {
		$AdminService.cancelImport(function() {
			$scope.isImporting = false;
		});
	}

	$scope.getImportUpdate = function() {
		$AdminService.getImportUpdate(function(isImporting) {
			$scope.isImporting = isImporting;
		});
	}


});
