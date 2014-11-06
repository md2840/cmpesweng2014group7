services.factory('AngucompleteServiceLogin',['$http',function($http){
	return {
		call: function(url,scope,str){

		/**
		 * Caseler içindeki success methodlarının çağrılma şekilleri angucomplete.js'in çalışması için gereklidir
		 */
			switch(url){
			case 'searchPanel_searchName':
				console.log("anguService");
				$http.post('Index/searchName',{params: {
					query: str
				}}).
				success(function(responseData, status, headers, config) {
					scope.searching = false;
					scope.processResults(responseData.result);
				}).
				error(function(data, status, headers, config) {
					console.log("error");
				});
				break;
			case 'searchPanel_searchTag':
				$http.post('/UrbSource/experience/searchTag',{params: {
					query: str
				}}).
				success(function(responseData, status, headers, config) {
					scope.searching = false;
					scope.processResults(responseData.tagList);
				}).
				error(function(data, status, headers, config) {
					console.log("error");
				});
				break;
			}
		}
	};
}
]);