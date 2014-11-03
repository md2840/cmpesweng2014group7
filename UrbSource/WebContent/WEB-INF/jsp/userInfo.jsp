<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="us" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<us:page user="${command}">
<jsp:attribute name="mainPanel">
	<form:form method="POST">
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
				</c:choose>
			</div>
		</c:if>
		<form:hidden path="username" />
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
			Karma Points: ${user.karma}
		</p>
		<p>
			Experience Points: ${user.experiencePoints}
		</p>
		<p>
			Comment Points: ${user.commentPoints}
		</p>
		<p>
			<a href="/UrbSource/experience/user/${user.id}">You have ${user.numberOfExperiences} experiences</a>
		</p>
		<p>
			<input type="submit" value="Update Information" class="btn btn-primary">
		</p>
	</form:form>
</jsp:attribute>
</us:page>