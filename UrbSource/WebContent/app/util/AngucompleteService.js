services.factory('AngucompleteService',['$http',function($http){
	return {
		call: function(url,scope,str){

			switch(url){
			case 'searchPanel-searchName':
				console.log("ara beni optum seni");
				break;
			}
		}
	};
}
]);