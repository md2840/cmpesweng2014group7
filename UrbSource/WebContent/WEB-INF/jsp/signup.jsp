<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html data-ng-app="urbsource">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>UrbSource</title>
<!-- RESOURCES -->


<!-- JS -->
<script src="../resources/jquery-1.11.1.js"></script>
<script src="../resources/angular/angular.js"></script>
<script src="../resources/angular/ui-bootstrap-tpls-0.11.0.js"></script>
<script src="../resources/js/ng-grid.debug.js"></script>

<!--  CSS -->
<link rel="stylesheet" type="text/css" href="../resources/css/ng-grid.css">
<link rel="stylesheet" type="text/css"
	href="../resources/css/bootstrap-combined.min.css">
<link rel="stylesheet" type="text/css"
	href="../resources/css/angucomplete.css">
<link rel="stylesheet" type="text/css"
	href="../resources/css/urbSource.css">


<!-- APP -->
<script src="../app/app.js"></script>

</head>
<body>
	<h1>Sign Up!</h1>
	<h2>Why don't you join and start sharing your experiences?</h2>
	<div id="container">
	<form:form action="/UrbSource/signup/confirm" method="POST">
		<%
		if (request.getAttribute("error") != null) {
		%>
		<div class="alert alert-danger" role="alert">
			<%
			if (request.getAttribute("error").equals("invalid_email")) {
			%>
			Invalid email address.
			<%
			} else if (request.getAttribute("error").equals("password_mismatch")) {
			%>
			Passwords are not matching.
			<%
			} else if (request.getAttribute("error").equals("empty_password")) {
			%>
			Passwords is empty.
			<%
			} else  {
			%>
			The username or e-mail address is already taken.
			<%
			}
			%>
		</div>
		<%
		}
		%>
		<p>
			<label>Username: <form:input path="username" /></label>
		</p>
		<p>
			<label>E-mail address: <form:input path="email" /></label>
		</p>
		<p>
			<label>First Name: <form:input path="firstName" /></label>
		</p>
		<p>
			<label>Last Name: <form:input path="lastName" /></label>
		</p>
		<p>
			<label>Password: <form:password path="password" /></label>
		</p>
		<p>
			<label>Password (again): <form:password path="password2" /></label>
		</p>
		<p>
			<input type="submit" value="Sign Up!">
			<input type="reset" value="Clear Form">
		</p>
	</form:form>
	</div>
</body>
</html>