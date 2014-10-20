directives.directive('usSearchPanel',function(){
	return {
		restrict: 'EAC',
		templateUrl: "app/login/mainPage/searchPanel/searchPanel.html",
	};
});

directives.directive('usLoginUserPanel',function(){
	return {
		restrict: 'EAC',
		templateUrl: "app/login/mainPage/loginUser/loginUser.html",
	};
});

directives.directive('usMainPanel',function(){
	return {
		restrict: 'EAC',
		templateUrl: "app/login/mainPage/mainPanel/mainPanel.html",
	};
});