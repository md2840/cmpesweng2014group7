controllers.controller('ExperienceListController',['$scope','SendExperienceService','SearchExperienceFactory',function($scope,SendExperienceService,SearchExperienceFactory){
	$scope.experienceList = {};
	$scope.search = false;
	
	SearchExperienceFactory.getRecentExperiences($scope);
	
	SendExperienceService.observe().then(null,null,function(experienceList){
		$scope.experienceList = experienceList;
		$scope.search = true;
	});
}]);