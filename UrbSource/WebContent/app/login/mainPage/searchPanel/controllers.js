controllers.controller('searchExperienceCtrl',['$scope','SearchExperienceFactory','SendExperienceService',function($scope,SearchExperienceFactory,SendExperienceService){
	$scope.tag = {};
	$scope.text = undefined;
	$scope.by = "text";
	$scope.tag = undefined;
	$scope.location = undefined;

	$scope.selectBy = function(by){
		$scope.by = by;
	};
		
	$scope.searchExp = function(){
		
		if($scope.by === 'text'){
			SearchExperienceFactory.getTextBaseExp($scope);
		}
		else if($scope.by === 'tag'){
			if($scope.tag.originalObject){
				SearchExperienceFactory.getTagBaseExp($scope);
			}
			else
				console.log("burda tag seçmedin alerti at");
			//TODO
		} else
		{
			console.log('location araması');
			}
	};
}]);