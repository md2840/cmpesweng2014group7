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
		
		<p style="z-index:99999">
			<label>Username: <form:input path="username" /></label>
		</p>
		<p style="z-index:99999">
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
			<sc:captcha/>
		</p>
		
		
		<p>
			
			<input type="submit" value="Sign Up!">
			<input type="reset" value="Clear Form">
		</p>
	</form:form>
	
</jsp:attribute>
</us:page>