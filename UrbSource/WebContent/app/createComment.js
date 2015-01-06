/**
 * Create a comment when pressed "Add Comment" button
 *
 * @author: Mehmet Emre
 */

jQuery(function($) {
    $('button#add-comment').click(function () {
        var text = $('textarea#comment-text').val().trim();
        if (text === '') {
            alert('You must enter comment content to share it!');
            return;
        }
        $.ajax({
            'type': 'POST',
            'url': '/UrbSource/comment/create',
            'contentType': 'application/json; charset=utf-8',
            'data': JSON.stringify({
                text: text,
                experienceId : window.experienceId
            }),
            'dataType': 'json'
        }).done(function (resp) {
            if (resp.success) {
            	location.reload();
                // TODO: Automatically add new experience to list
            }
            else {
                alert('An internal error ocurred:\n\n' + resp.error);
            }
        }).fail(function () {
            alert('An internal error has occurred. Please try again later');
        }).always(function (resp) {
        	console.log(resp);
        });
    });
});