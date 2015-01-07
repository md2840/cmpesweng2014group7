<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="us" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="sc" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<us:page user="${command}">
<jsp:attribute name="mainPanel">
	<form:form action="/UrbSource/signup/confirm" method="POST">
		<c:set var="error" value="${error}" />
		<c:if test="${!empty error}">
			<div class="alert alert-danger" role="alert">
				<c:choose>
					<c:when test="${error == 'invalid_email'}">
					Invalid email address.
					</c:when>
					<c:when test="${error == 'password_mismatch'}">
				Passwords are not matching.
					</c:when>
					<c:when test="${error == 'empty_password'}">
						Password is empty.
					</c:when>
					
					<c:otherwise>
				The username or e-mail address is already taken.
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>
		
		<div style="z-index:99999" class="form-group">
			<label for="username">Username:</label>
			<form:input path="username" class="form-control" />
		</div>
		<div style="z-index:99999" class="form-group">
			<label>E-mail address: <form:input path="email"  class="form-control"/></label>
		</div>
		<div style="z-index:99999" class="form-group">
			<label>First Name: <form:input path="firstName" class="form-control" /></label>
		</div>
		<div style="z-index:99999" class="form-group">
			<label>Last Name: <form:input path="lastName" class="form-control" /></label>
		</div>
		<div style="z-index:99999" class="form-group">
			<label>Password: <form:password path="password"  class="form-control"/></label>
		</div>
		<div style="z-index:99999" class="form-group">
			<label>Password (again): <form:password path="password2"  class="form-control"/></label>
		</div>
		<div style="z-index:99999" class="form-group">
			<sc:captcha/>
		</div>
		<div style="z-index:99999" class="form-group">
			<input type="submit" class="btn btn-primary" value="Sign Up!">
			<input type="reset" class="btn btn-danger" value="Clear Form">
		</div>
	</form:form>
	
</jsp:attribute>
</us:page>