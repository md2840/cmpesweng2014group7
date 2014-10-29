<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="us" tagdir="/WEB-INF/tags" %>
<%@ attribute name="user" required="true" %>
<%@ attribute name="mainPanel" fragment="true" required="true" %>
<%@ attribute name="head" fragment="true" required="false" %>
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

<!--  CSS -->
<link rel="stylesheet" type="text/css" href="/UrbSource/resources/css/ng-grid.css">
<link rel="stylesheet" type="text/css"
	href="/UrbSource/resources/css/bootstrap-combined.min.css">
<link rel="stylesheet" type="text/css"
	href="/UrbSource/resources/css/angucomplete.css">
<link rel="stylesheet" type="text/css"
	href="/UrbSource/resources/css/urbSource.css">

<!-- APP -->
<script src="/UrbSource/app/app.js"></script>
<script src="/UrbSource/app/util/AngucompleteService.js"></script>
<!-- MAIN PAGE -->
<script src="/UrbSource/app/mainPage/js/directives.js"></script>
<script src="/UrbSource/app/mainPage/js/controllers.js"></script>
<script src="/UrbSource/app/mainPage/searchPanel/controllers.js"></script>

<jsp:invoke fragment="head" />
</head>
<body>
	<div class="containSearchLogin" ng-controller="URLCtrl">
		<us:search-panel></us:search-panel>
		<us:user-info user="${user}"></us:user-info>
	</div>
	<div class="mainPanel">
		<jsp:invoke fragment="mainPanel"/>
	</div>
</body>
</html>