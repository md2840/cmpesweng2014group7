controllers.controller('searchExperienceCtrl',['$scope','SearchExperienceFactory','SendExperienceService',function($scope,SearchExperienceFactory,SendExperienceService){
	$scope.tag = {};
	$scope.text = undefined;
	$scope.by = "text";
	$scope.tag = undefined;
	$scope.location = undefined;
	$scope.tagArray = [];

	$scope.selectBy = function(by){
		$scope.by = by;
	};
		
	$scope.searchExp = function(){
		
		if($scope.by === 'text'){
			SearchExperienceFactory.getTextBaseExp($scope);
		}
		else if($scope.by === 'tag'){
			
			var i = 0;
			
			while($scope.tag[i]){
				$scope.tagArray.push($scope.tag[i].originalObject);
				i++;
			}
			
			if($scope.tagArray.length)
				SearchExperienceFactory.getTagBaseExp($scope);
		
		} else
		{
			console.log('location araması');
			}
	};
}]);