services.factory('SearchExperienceFactory',['$http','SendExperienceService',function($http,SendExperienceService){
	return {
		getTagBaseExp: function($scope){
			
			console.log($scope.tagArray.length);
			var request = $http.post('/UrbSource/experience/searchExperienceTag',{
				params: {
					tags: $scope.tagArray
				}
			});

			request.success(function(responseData, status, headers, config){
				console.log(responseData.experiences);
				SendExperienceService.send(responseData.experiences);
			});

			request.error(function(responseData, status, headers, config){

			});
		},
		getTextBaseExp: function($scope){
			var request = $http.post('/UrbSource/experience/searchExperienceText',{
				params: {
					text: $scope.text,
				}
			});

			request.success(function(responseData, status, headers, config){
				SendExperienceService.send(responseData.experienceList);
			});

			request.error(function(responseData, status, headers, config){

			});
		},
		getRecentExperiences: function($scope){
			var request = $http.get('/UrbSource/experience/recent');

			request.success(function(responseData, status, headers, config){
				$scope.experienceList = responseData.experiences;
			});

			request.error(function(responseData, status, headers, config){

			});
		},
		getUserExperiences: function($scope){
			var id = document.URL.split('/').slice(-1)[0];
			if (id !== (Number.parseInt(id)) + '' || ! (Number.parseInt(id) > 0)) {
				return;
			}
			var request = $http.get('/UrbSource/experience/user/json/' + id);

			request.success(function(responseData, status, headers, config){
				$scope.userExperienceList = responseData.experiences;
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

services.factory('DelEditExperienceFactory',['$http',function($http){
	return {
		deleteExp: function($scope,id){
			var request = $http.post('/UrbSource/experience/delete',{
				params: {
					id:id
				}
			});
			
			toastr.options = {
					"closeButton": false,
					"debug": false,
					"positionClass": "toast-bottom-right",
					"onclick": null,
					"showDuration": "300",
					"hideDuration": "1000",
					"timeOut": "0",
					"extendedTimeOut": "0",
					"showEasing": "swing",
					"hideEasing": "linear",
					"showMethod": "fadeIn",
					"hideMethod": "fadeOut"
			};

			request.success(function(responseData, status, headers, config){
					var request = $http.get('/UrbSource/experience/recent');

					request.success(function(responseData, status, headers, config){
						$scope.experienceList = responseData.experiences;
						toastr.success("Delete is successfully done", 'Please wait then click anywhere to refresh');
					});

					request.error(function(responseData, status, headers, config){

					});
			});

			request.error(function(responseData, status, headers, config){});
		},
		editExp: function($scope,id,text){
			var request = $http.post('/UrbSource/experience/editText',{
				params: {
					id:id,
					text:text
				}
			});
			
			toastr.options = {
					"closeButton": false,
					"debug": false,
					"positionClass": "toast-bottom-right",
					"onclick": null,
					"showDuration": "300",
					"hideDuration": "1000",
					"timeOut": "0",
					"extendedTimeOut": "0",
					"showEasing": "swing",
					"hideEasing": "linear",
					"showMethod": "fadeIn",
					"hideMethod": "fadeOut"
			};

			request.success(function(responseData, status, headers, config){
					var request = $http.get('/UrbSource/experience/recent');

					request.success(function(responseData, status, headers, config){
						$scope.experienceList = responseData.experiences;
						toastr.success("Edit is successfully done", 'Please wait then click anywhere to refresh');
					});

					request.error(function(responseData, status, headers, config){

					});
			});

			request.error(function(responseData, status, headers, config){});
		}
	};
}]);
