directives.directive('usSearchPanel',function(){
	return {
		restrict: 'EAC',
		templateUrl: "app/mainPage/html/searchPanel.html",
	};
});

directives.directive('usLoginUserPanel',function(){
	return {
		restrict: 'EAC',
		templateUrl: "app/mainPage/html/loginUser.html",
	};
});

directives.directive('usMainPanel',function(){
	return {
		restrict: 'EAC',
		templateUrl: "app/mainPage/html/mainPanel.html",
	};
});