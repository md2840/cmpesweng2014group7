services.factory('AngucompleteService',['$http',function($http){
	return {
		call: function(url,scope,str){

		/**
		 * Caseler içindeki success methodlarının çağrılma şekilleri angucomplete.js'in çalışması için gereklidir
		 */
			switch(url){
			case 'searchPanel-searchName':
				$http.get('Index/searchName',{params: {
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
			}
		}
	};
}
]);