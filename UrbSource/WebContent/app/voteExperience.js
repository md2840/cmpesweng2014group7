/**
 * Experience upvote/downvote functions.
 */


services.factory('VoteFactory',['$http',function($http) {
	return {
		upvote: function(exp, element) {
			var undo = $(element).hasClass('btn-primary');
			$(element).parent().children().prop("disabled", true);
			$.ajax({
		        'type': 'POST',
		        'url': '/UrbSource/experience/upvote',
		        'contentType': 'application/json; charset=utf-8',
		        'data': JSON.stringify({
		            id: exp,
		            undo: undo
		        }),
		        'dataType': 'json'
		    }).done(function (resp) {
		        if (resp.success) {
		        	$(element).toggleClass("btn-primary");
		        	$(element).toggleClass("btn-default");
		        }
		        else {
			        alert('An internal error has occurred. Please try again later');
		        }
		    }).fail(function () {
		        alert('An internal error has occurred. Please try again later');
		    }).always(function (resp) {
		    	console.log(resp);
				$(element).parent().children().prop("disabled", false);
		    });
		},
		downvote: function(exp, element) {
			var undo = $(element).hasClass('btn-primary');
			$(element).parent().children().prop("disabled", true);
			$.ajax({
		        'type': 'POST',
		        'url': '/UrbSource/experience/downvote',
		        'contentType': 'application/json; charset=utf-8',
		        'data': JSON.stringify({
		            id: exp,
		            undo: undo
		        }),
		        'dataType': 'json'
		    }).done(function (resp) {
		        if (resp.success) {
		        	$(element).toggleClass("btn-primary");
		        	$(element).toggleClass("btn-default");
		        }
		        else {
			        alert('An internal error has occurred. Please try again later');
		        }
		    }).fail(function () {
		        alert('An internal error has occurred. Please try again later');
		    }).always(function (resp) {
		    	console.log(resp);
				$(element).parent().children().prop("disabled", false);
		    });
		}
	};
}]);