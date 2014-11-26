controllers.controller('ExperienceListController',['$scope','DelEditExperienceFactory','SendExperienceService','SearchExperienceFactory',function($scope,DelEditExperienceFactory,SendExperienceService,SearchExperienceFactory){
	$scope.experienceList = {};
	$scope.search = false;
	
	SearchExperienceFactory.getRecentExperiences($scope);
	
	SendExperienceService.observe().then(null,null,function(experienceList){
		$scope.experienceList = experienceList;
		$scope.search = true;
	});
	
	$scope.deleteExp = function(id){
		DelEditExperienceFactory.deleteExp($scope,id);
	};
}]);