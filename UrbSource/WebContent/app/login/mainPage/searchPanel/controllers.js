controllers.controller('searchExperienceCtrl',['$scope','SearchExperienceFactory','SendExperienceService',function($scope,SearchExperienceFactory,SendExperienceService){
	$scope.tag = {};
	$scope.text = undefined;

	$scope.searchExp = function(){
		
		SendExperienceService.close();
		
		if($scope.text){
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
		}
	};
}]);