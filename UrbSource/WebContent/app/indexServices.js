services.factory('UserFactory',['$http',function($http){
	return {
		getNames: function(scope) {
			$http.post('Index/getNames').success(function(response){
				scope.usernames = response.result;
			});
		},
		addName: function(scope){
			$http.post('Index/addName',{
				params: {
					N_NAME: scope.name
				}
			}).success(function(data,status,headers,config){
				console.log(data);
				if(data.success){
					$http.post('Index/getNames').success(function(response){
						scope.usernames = response.result;
					});
				}
			});
		},
	}
}]);