controllers.controller('searchExperienceCtrl',['$scope',function($scope){
	$scope.tag = {};
	$scope.text = undefined;

	$scope.searchExp = function(){
		console.log("girdim");
		console.log($scope.text);

		if($scope.tag){
			if($scope.tag.originalObject){
				console.log("tag ara");
			} else {
				console.log("text ara");	
			}
		}
		else {
			console.log("text ara");	
		}
	};
}]);