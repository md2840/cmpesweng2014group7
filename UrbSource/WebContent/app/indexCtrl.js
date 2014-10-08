controllers.controller('indexCtrl',['$scope','UserFactory',function($scope,UserFactory){
	
	$scope.name = null;
	UserFactory.getNames($scope);
	$scope.usernameGridOptions = {
		data: 'usernames',
		selectedItems: [],
		maintainColumnRatios: false,
		multiSelect: false,
		showSelectionCheckbox: false,
		selectWithCheckboxOnly: false,
		plugins: [],
		columnDefs: [{field: 'N_NAME', displayName: 'Name'}]
	};

	$scope.addName = function(){
		UserFactory.addName($scope);
	};
}]);