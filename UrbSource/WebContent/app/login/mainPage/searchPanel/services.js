services.factory('SearchExperienceFactory',['$http',function($http){
	return {
		getTagBaseExp: function($scope){
			var request = $http.post('experience/searchExperienceTag',{
				params: {
					id: $scope.tag.originalObject.id,
					name: $scope.tag.originalObject.name
				}
			});
			
			request.success(function(responseData, status, headers, config){
				console.log("bu expleri mainpage'e yolla");
				console.log(responseData);
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
				console.log("bu expleri mainpage'e yolla");
				console.log(responseData);
			});
			
			request.error(function(responseData, status, headers, config){
				
			});
		}
	};
}]);