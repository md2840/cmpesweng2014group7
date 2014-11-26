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
	
	$scope.save = function(id,$event){
		$event.target.parentElement.parentElement.childNodes[3].childNodes[2].setAttribute("contentEditable","true");
		$event.target.parentElement.parentElement.childNodes[3].childNodes[3].style.display = "none";
		DelEditExperienceFactory.editExp($scope,id,$event.target.parentElement.parentElement.childNodes[3].childNodes[2].innerText);
	};
}]);