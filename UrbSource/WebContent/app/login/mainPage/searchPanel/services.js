services.factory('SearchExperienceFactory',['$http','SendExperienceService',function($http,SendExperienceService){
	return {
		getTagBaseExp: function($scope){
			var request = $http.post('experience/searchExperienceTag',{
				params: {
					id: $scope.tag.originalObject.id,
					name: $scope.tag.originalObject.name
				}
			});
			
			request.success(function(responseData, status, headers, config){
				SendExperienceService.send(responseData.experienceList);
			});
			
			request.error(function(responseData, status, headers, config){
				
			});
		},
		getTextBaseExp: function($scope){
			var request = $http.post('experience/searchExperienceText',{
				params: {
					text: $scope.text,
				}
			});
			
			request.success(function(responseData, status, headers, config){
				SendExperienceService.send(responseData.experienceList);
			});
			
			request.error(function(responseData, status, headers, config){
				
			});
		}
	};
}]);

services.service('SendExperienceService',['$q',function($q){
	var defer= $q.defer();
	
	this.send = function(experienceList){
		return defer.notify(experienceList);
	};
	
	 this.observe = function(){
		return defer.promise;
	};
	
	var defer2 = $q.defer();
	
	this.close = function(){
		return defer2.notify();
	};
	
	 this.observeDrop = function(){
		return defer2.promise;
	};
	
}]);