/**
 * Create experience when pressed share button.
 *
 * @author: Mehmet Emre
 */

jQuery(function($) {
    $('button#create-experience').click(function () {
        var text = $('textarea#experience-text').val().trim();
        var mood = $('select#experience-mood').val().trim();
        var tags = $('input#experience-tags').val().trim().toLowerCase().split(',');
        if (text === '') {
            alert('You must enter experience content to share it!');
            return;
        }
        for (var i in tags) {
            tags[i] = tags[i].trim();
        }
        $.ajax({
            'type': 'POST',
            'url': '/UrbSource/experience/create',
            'contentType': 'application/json; charset=utf-8',
            'data': JSON.stringify({
                text: text,
                tags: tags,
                mood: mood
            }),
            'dataType': 'json'
        }).done(function (resp) {
            if (resp.success) {
            	location.reload();
                // TODO: Automatically add new experience to list
            }
            else {
                alert('Error', resp.error);
            }
        }).fail(function () {
            alert('An internal error has occurred. Please try again later');
        }).always(function (resp) {
        	console.log(resp);
        });
    });
});