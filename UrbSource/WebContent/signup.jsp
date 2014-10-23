<!DOCTYPE html>
<html data-ng-app="urbsource">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>UrbSource</title>
<!-- RESOURCES -->

<!-- JS -->
<script src="resources/jquery-1.11.1.js"></script>
<script src="resources/angular/angular.js"></script>
<script src="resources/angular/ui-bootstrap-tpls-0.11.0.js"></script>
<script src="resources/js/ng-grid.debug.js"></script>

<!--  CSS -->
<link rel="stylesheet" type="text/css" href="resources/css/ng-grid.css">
<link rel="stylesheet" type="text/css"
	href="resources/css/bootstrap-combined.min.css">
<link rel="stylesheet" type="text/css"
	href="resources/css/angucomplete.css">
<link rel="stylesheet" type="text/css"
	href="resources/css/urbSource.css">


<!-- APP -->
<script src="app/app.js"></script>

</head>
<body>
	<h1>Sign Up!</h1>
	<h2>Why don't you join and start sharing your experiences?</h2>
	<div id="container">
	<form action="/UrbSource/signup/confirm.jsp" method="POST">
		<p>
			<label>Username: <input type="text" id="" name="username"/></label>
		</p>
		<p>
			<label>E-mail address: <input type="text" id="" name="email"/></label>
		</p>
		<p>
			<label>First Name: <input type="text" id="" name="firstName"/></label>
		</p>
		<p>
			<label>Last Name: <input type="text" id="" name="lastName"/></label>
		</p>
		<p>
			<label>Password: <input type="password" id="" name="password"/></label>
		</p>
		<p>
			<label>Password (again): <input type="password" id="" name="password2"/></label>
		</p>
		<p>
			<input type="submit" value="Sign Up!">
			<input type="reset" value="Clear Form">
		</p>
	</form>
	</div>
</body>
</html>