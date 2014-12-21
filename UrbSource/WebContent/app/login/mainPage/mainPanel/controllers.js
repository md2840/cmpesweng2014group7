controllers.controller('ExperienceListController',
		['$scope', 'VoteFactory', 'DelEditExperienceFactory', 'SendExperienceService', 'SearchExperienceFactory',
		 function($scope, VoteFactory, DelEditExperienceFactory, SendExperienceService, SearchExperienceFactory) {
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

	$scope.upvote = function(id, element, undo){
		VoteFactory.upvote(id, element, undo);
	}
	
	$scope.downvote = function(id, element, undo){
		VoteFactory.downvote(id, element, undo);
	}
	
	$scope.reportSpam = function(id,$event) {
		$.ajax({
			url:"/UrbSource/experience/markSpam",
			data: JSON.stringify({
				id: id
			}),
			type: 'POST',
			contentType: 'application/json; charset=UTF-8',
            'dataType': 'json'
		}).done(function(responseData, status, jqXHR){
			if (responseData.success) {
				$($event.target).hide();
			}
		});
	};
	
	$scope.save = function(id,$event){
		$event.target.parentElement.childNodes[2].setAttribute("contentEditable","false");
		$event.target.parentElement.childNodes[3].style.display = "none";
		DelEditExperienceFactory.editExp($scope,id,$event.target.parentElement.childNodes[2].innerText);
	};
}]);