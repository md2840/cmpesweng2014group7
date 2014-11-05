controllers.controller('ExperienceListController',['$scope','SendExperienceService',function($scope,SendExperienceService){
	$scope.experienceList = {};
	$scope.search = false;
	
	
	SendExperienceService.observe().then(null,null,function(experienceList){
		$scope.experienceList = experienceList;
		$scope.search = true;
	});
}]);