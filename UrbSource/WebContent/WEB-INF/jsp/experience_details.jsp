<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="us" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<us:page user="${user}">
	<jsp:attribute name="head">
	<script>
		$(function() {

			var lastCheck = Date.now() + 1000; // One second bias
			var timeInterval = 10 * 1000;

			toastr.options = {
				"closeButton" : false,
				"debug" : false,
				"positionClass" : "toast-bottom-right",
				"onclick" : null,
				"showDuration" : "300",
				"hideDuration" : "1000",
				"timeOut" : "0",
				"extendedTimeOut" : "0",
				"showEasing" : "swing",
				"hideEasing" : "linear",
				"showMethod" : "fadeIn",
				"hideMethod" : "fadeOut"
			}

			var callback = function(response) {
				console.log(response);
				var nsize = response.notifications.length;
				for ( var i = 0; i < nsize; i++) {
					toastr.info(response.notifications[i].text,
							response.notifications[i].user.username).click(
							function() { // Read notification
								$.ajax({
									type : 'POST',
									url : '/UrbSource/notification/delete/'
								});
							});

				}
				//response.forEach(function () {
				//	toastr.info(reponse.notifications.text);
				/* toastr.info(notifications.text).click(function() { // Read notification
					$.ajax({
						type: 'GET',
						url: '/UrbSource/notification/all/'
					} );
				});*/
				//});
			};

			// Initial notification check
			$.ajax({
				type : 'GET',
				url : '/UrbSource/notification/all/'
			}).done(callback);

		});
		
		function editExp(el) {
			el.parentElement.parentElement.parentElement.parentElement.childNodes[3].childNodes[2].setAttribute("contentEditable","true");
			el.parentElement.parentElement.parentElement.parentElement.childNodes[3].childNodes[3].style.display = "block";
		}
		
		window.experienceId = ${experience.id};
	</script>
	<script>
		function initialize2() {
		  var num=document.getElementById('loc').value; 
		  var latlngStr = num.split(',', 2);
		  var lat = parseFloat(latlngStr[0]);
		  var lng = parseFloat(latlngStr[1]);
		  console.log(num);
		  var myLatlng2 = new google.maps.LatLng(lat,lng);
		  var mapOptions2 = {
		    zoom: 12,
		    center: myLatlng2
		  }
		  var map2 = new google.maps.Map(document.getElementById('map-canvas-2'), mapOptions2);
		
		  var marker2 = new google.maps.Marker({
		      position: myLatlng2,
		      map: map2,
		      title: 'Hello World!'
		  });
		}
		
		google.maps.event.addDomListener(window, 'load', initialize2);

    </script>
    <script src="/UrbSource/app/createComment.js"></script>
</jsp:attribute>
	<jsp:attribute name="mainPanel">
		<div id="experience-list">
			<div class="panel panel-default">
				<div class="panel-heading">
					<span ng-switch on="'${experience.mood}'">
						<span ng-switch-when="good" style="color: green">Good Experience</span>
						<span ng-switch-when="bad" style="color: red">Bad Experience</span>
						<span ng-switch-default>${experience.mood} Experience</span>
					</span>
					by <a href="/UrbSource/user/info/${experience.author.id}">${experience.author.username}</a>
					on ${experience.creationTime}
				</div>
				<div class="${experience.source}"></div>
				<div class="panel-body">
					<p>
						<p>${experience.text}</p>
						<button style="float:right;display:none;" ng-click="save(${experience.id},$event)" type="button" class="btn btn-primary btn-xs">Save</button>
					</p>
					<p>
						Tags: <span>${experience.tagNames}</span>
					</p>
				</div>
				<div id="map-canvas-2" style="position:relative;z-index:9999;width:100%; height:300px;"></div>
				<div class="panel-footer clearfix">
					<div class="pull-left">
						<div class="btn-toolbar" style="display: inline-block !important;">
							<div class="btn-group" style="display: inline-block !important;">
							<c:choose>
								<c:when test="${experience.upvotedByUser}">
									<button type="button" ng-click="upvote(${experience.id}, $event)" class="btn btn-xs btn-primary glyphicon glyphicon-thumbs-up"></button>
								</c:when>
								<c:otherwise>
									<button type="button" ng-click="upvote(${experience.id}, $event)" class="btn btn-xs btn-default glyphicon glyphicon-thumbs-up"></button>
								</c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${experience.downvotedByUser}">
									<button type="button" ng-click="downvote(${experience.id}, $event)" class="btn btn-xs btn-primary glyphicon glyphicon-thumbs-down"></button>
								</c:when>
								<c:otherwise>
									<button type="button" ng-click="downvote(${experience.id}, $event)" class="btn btn-xs btn-default glyphicon glyphicon-thumbs-down"></button>
								</c:otherwise>
							</c:choose>
							</div>
						</div>
						~ ${experience.points} Points -
						<a href="/UrbSource/experience/id/${experience.id}">${experience.numberOfComments} Comments</a>
					</div>
					<div class="btn-toolbar pull-right">
						<div class="btn-group" ng-show="experience.author.username === '${user.username}'">
							<button type="button" class="btn btn-primary btn-xs" onclick="editExp(this)" id="edit-experience">Edit</button>
							<button type="button" class="btn btn-primary btn-xs" ng-click="deleteExp(experience.id)" id="delete-experience">Delete</button>
						</div>
						<div class="btn-group" ng-hide="experience.userMarkedSpam">
							<button type="button" class="btn btn-danger btn-xs" ng-click="reportSpam(experience.id, $event)" id="report-spam">Report Spam</button>
						</div>
					</div>
				</div>
				<input id="loc" type="hidden" value="${experience.location}"></input>
			</div>
			<div class="panel panel-primary">
				<div class="panel-heading">
					Comment on this experience
				</div>
				<div class="panel-body">
				<p>
				<label for="comment-text">What do you think about this experience?</label><br/>
				<textarea name="comment-text" id="comment-text" style="width:100%" rows="10" maxlength="1000"></textarea>
				</p>
				<small><em>Comment text can be at most 1,000 characters.</em></small>
				</div>
				<div class="panel-footer clearfix">
					<div class="btn-toolbar pull-right">
						<button type="button" class="btn btn-primary btn-xs" id="add-comment">Add Comment</button>  
					</div>
				</div>
				<input id="loc" type="hidden" value="${experience.location}"></input>
			</div>
			<c:forEach items="${comments}" var="comment">
			<div class="panel panel-default">
				<div class="panel-heading">
					Comment by <a href="/UrbSource/user/info/${comment.author.id}">${comment.author.username}</a>
					on ${comment.creationTime}
				</div>
				<div class="panel-body">
					<p>
						<p>${comment.text}</p>
					</p>
				</div>
				<div class="panel-footer clearfix">
					<div class="btn-toolbar pull-right">
					<c:if test="${user.id == comment.author.id}">
						<button type="button" class="btn btn-danger btn-xs" id="delete-comment" onclick="deleteComment(${comment.id}, this)">Delete Comment</button>  
					</c:if>
					</div>
				</div>
				<input id="loc" type="hidden" value="${experience.location}"></input>
			</div>
			</c:forEach>
		</div>
</jsp:attribute>
</us:page>
