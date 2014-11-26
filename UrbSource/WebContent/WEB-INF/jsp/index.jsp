<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="us" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<us:page user="${user}">
<jsp:attribute name="head">
  <script>

  $(function () {
	  
		var lastCheck = Date.now()+1000; // One second bias
		var timeInterval = 10*1000;

		toastr.options = {
			"closeButton": false,
			"debug": false,
			"positionClass": "toast-bottom-right",
			"onclick": null,
			"showDuration": "300",
			"hideDuration": "1000",
			"timeOut": "0",
			"extendedTimeOut": "0",
			"showEasing": "swing",
			"hideEasing": "linear",
			"showMethod": "fadeIn",
			"hideMethod": "fadeOut"
		}

		var callback = function (response) {
			console.log(response);
			var nsize=response.notifications.length;
			for(var i=0;i<nsize;i++){
				toastr.info(response.notifications[i].text,response.notifications[i].user.username).click(function() { // Read notification
					$.ajax({
						type: 'POST',
						url: '/UrbSource/notification/delete/'
					} );
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
			type: 'GET',
			url: '/UrbSource/notification/all/'
		}).done(callback);
        
            
    });
 </script> 
</jsp:attribute>
<jsp:attribute name="mainPanel">
		<div ng-controller="ExperienceListController">
		<div id="recent-experience-list">
		<c:forEach var="experience" items="${experiences}">
			<div class="panel panel-default">
				<div class="panel-heading">By: <a href="/UrbSource/user/info/${experience.author.id}">${experience.author.username}</a></div>
				<div class="panel-body">
				<p>
					${experience.text}
				</p>
				<p>
					Tags: 
					
					<c:forEach var="tag" items="${experience.tags}">
					<span>${tag.name}, </span>
					</c:forEach>
				</p>
				</div>
			</div>
		</c:forEach>
		</div>
		</div>
		<div ng-show="search">Search Results:<br>
		<div id="experience-list">
			<div class="panel panel-default" ng-repeat="experience in experienceList">
				<div class="panel-heading">By: <a href="/UrbSource/user/info/{{experience.author.id}}">{{experience.author.username}}</a></div>
				<div class="panel-body">
				<p>
					{{experience.text}}
				</p>
				<p>
					Tags: <span ng-repeat="tag in experience.tags">{{tag.name}}, </span>
				</p>
				</div>
			</div>
		</div>
		</div>
	</div>
</jsp:attribute>
</us:page>