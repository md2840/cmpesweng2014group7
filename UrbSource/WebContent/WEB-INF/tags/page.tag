<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="us" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ attribute name="mainPanel" fragment="true" required="true" %>
<%@ attribute name="head" fragment="true" required="false" %>
<%@ attribute name="user" required="true" type="com.urbsource.models.User" %>
<!DOCTYPE html>
<html data-ng-app="urbsource">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>UrbSource</title>
<!-- RESOURCES -->

<!-- JS -->
<script src="/UrbSource/resources/jquery-1.11.1.js"></script>
<script src="/UrbSource/resources/angular/angular.js"></script>
<script src="/UrbSource/resources/angular/ui-bootstrap-tpls-0.11.0.js"></script>
<script src="/UrbSource/resources/angular/angucomplete.js"></script>
<script src="/UrbSource/resources/js/ng-grid.debug.js"></script>
<script src="/UrbSource/resources/js/toastr.js"></script>

<!--  CSS -->
<link rel="stylesheet" type="text/css" href="/UrbSource/resources/css/ng-grid.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css"
	href="/UrbSource/resources/css/angucomplete.css">
<link rel="stylesheet" type="text/css"
	href="/UrbSource/resources/css/urbSource.css">
<link rel="stylesheet" type="text/css"
	href="/UrbSource/resources/css/toastr.css">
	
<!-- APP -->
<script src="/UrbSource/app/app.js"></script>
<script src="/UrbSource/app/util/AngucompleteService.js"></script>
<!-- MAIN PAGE -->
<script src="/UrbSource/app/mainPage/js/directives.js"></script>
<script src="/UrbSource/app/mainPage/js/controllers.js"></script>
<script src="/UrbSource/app/mainPage/searchPanel/controllers.js"></script>

<sec:authorize access="isAuthenticated()">
	<script src="/UrbSource/app/createExperience.js"></script>
</sec:authorize>

<jsp:invoke fragment="head" />
</head>
<body>
	<div class="containSearchLogin" ng-controller="URLCtrl">
		<us:search-panel></us:search-panel>
		<us:user-info user="${user}"></us:user-info>
	</div>
	<div class="mainPanel">
		<sec:authorize access="isAuthenticated()">
			<us:create-experience></us:create-experience>
		</sec:authorize>
		<jsp:invoke fragment="mainPanel"/>
	</div>
</body>
</html>