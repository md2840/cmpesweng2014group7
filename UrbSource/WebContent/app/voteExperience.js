/**
 * Experience upvote/downvote functions.
 */


services.factory('VoteFactory',['$http',function($http) {
	return {
		upvote: function(exp, element, undo) {
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
		        	if (undo)
		        		$(element).addClass("btn-primary");
		        	else
		        		$(element).removeClass("btn-primary");
		        }
		        else {
		            alert('Error', resp.error);
		        }
		    }).fail(function () {
		        alert('An internal error has occurred. Please try again later');
		    }).always(function (resp) {
		    	console.log(resp);
		    });
		},
		downvote: function(exp, element, undo) {
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
		        	if (undo)
		        		$(element).addClass("btn-primary");
		        	else
		        		$(element).removeClass("btn-primary");
		        }
		        else {
		            alert('Error', resp.error);
		        }
		    }).fail(function () {
		        alert('An internal error has occurred. Please try again later');
		    }).always(function (resp) {
		    	console.log(resp);
		    });
		}
	};
}]);