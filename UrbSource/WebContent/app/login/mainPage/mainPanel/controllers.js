controllers.controller('ExperienceListController',
		['$scope', 'VoteFactory', 'DelEditExperienceFactory', 'SendExperienceService', 'SearchExperienceFactory',
		 function($scope, VoteFactory, DelEditExperienceFactory, SendExperienceService, SearchExperienceFactory) {
	$scope.experienceList = {};
	$scope.search = false;
	if (!String.prototype.includes) {
		String.prototype.includes = function() {'use strict';
			return String.prototype.indexOf.apply(this, arguments) !== -1;
		};
	}

	SearchExperienceFactory.getRecentExperiences($scope);
	if (document.URL.includes('experience/user'))
		SearchExperienceFactory.getUserExperiences($scope);
	
	SendExperienceService.observe().then(null,null,function(experienceList){
		$scope.experienceList = experienceList;
		$scope.search = true;
	});
	
	$scope.deleteExp = function(id){
		DelEditExperienceFactory.deleteExp($scope,id);
	};

	$scope.upvote = function(id, $event){
		VoteFactory.upvote(id, $event.target);
	}
	
	$scope.downvote = function(id, $event){
		VoteFactory.downvote(id, $event.target);
	}
	
	$scope.reportSpam = function(id, $event) {
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