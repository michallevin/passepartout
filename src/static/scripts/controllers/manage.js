'use strict';

/**
 * @ngdoc function
 * @name passepartoutApp.AdminCrl
 * @description
 * # AdminCrl
 * Controller of the passepartoutApp
 */
angular.module('passepartoutApp')
.controller('AdminCtrl', function ($scope, $Admin, $interval) {

	$scope.isImporting = false;

	$scope.getStatus = function() {
		if ($scope.isImporting) return "Active!";
		return "Inactive";
	}
		
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


    $interval(function(){ $scope.getImportUpdate(); }, 3000);
	$scope.getImportUpdate();

});
