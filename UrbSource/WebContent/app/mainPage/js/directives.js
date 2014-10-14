directives.directive('usSearchPanel',function(){
	return {
		restrict: 'EAC',
		templateUrl: "app/mainPage/searchPanel/searchPanel.html",
	};
});

directives.directive('usLoginUserPanel',function(){
	return {
		restrict: 'EAC',
		templateUrl: "app/mainPage/loginUser/loginUser.html",
	};
});

directives.directive('usMainPanel',function(){
	return {
		restrict: 'EAC',
		templateUrl: "app/mainPage/mainPanel/mainPanel.html",
	};
});