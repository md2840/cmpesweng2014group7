<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="us" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<us:page user="${user}">
<jsp:attribute name="head">
  <script>

    $(document).ready(function() {
    	toastr.options = {
    			  "closeButton": false,
    			  "debug": false,
    			  "positionClass": "toast-bottom-right",
    			  "onclick": null,
    			  "showDuration": "3000",
    			  "hideDuration": "10000",
    			  "timeOut": "50000",
    			  "extendedTimeOut": "10000",
    			  "showEasing": "swing",
    			  "hideEasing": "linear",
    			  "showMethod": "fadeIn",
    			  "hideMethod": "fadeOut"
    			};
    	toastr.info("This is a great city! I would love to come again.", "Mustafa Demirel");
    	toastr.info("Best Iskender Doner I've ever had.", "Steve Jobs");
    	toastr.info("Traffic is so frustrating.", "Ilgın Yasar");

    });
 </script> 
</jsp:attribute>
<jsp:attribute name="mainPanel">
		<div ng-controller="ExperienceListController">
		<div ng-hide="search">Recent Experiences:<br>
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