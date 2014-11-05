controllers.controller('searchExperienceCtrl',['$scope','SearchExperienceFactory',function($scope,SearchExperienceFactory){
	$scope.tag = {};
	$scope.text = undefined;

	$scope.searchExp = function(){
		console.log($scope.text);

		if($scope.tag){
			if($scope.tag.originalObject){
				SearchExperienceFactory.getTagBaseExp($scope);
			} else {
				SearchExperienceFactory.getTextBaseExp($scope);
			}
		}
		else {
			SearchExperienceFactory.getTextBaseExp($scope);	
		}
	};
}]);