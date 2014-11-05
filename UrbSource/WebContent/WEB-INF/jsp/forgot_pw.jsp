<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="us" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<us:page user="${command}">
<jsp:attribute name="mainPanel">
	<form:form action="/UrbSource/user/forgot_pw" method="POST">
		<c:set var="error" value="${error}" />
		<c:if test="${!empty error}">
			<div class="alert alert-danger" role="alert">
				<c:choose>
					<c:when test="${error == 'invalid_email'}">
						Invalid email address.
					</c:when>
					<c:when test="${error == 'user_not_exist'}">
						This e-mail address is not found in the system.
					</c:when>
					<c:when test="${error == 'empty_email'}">
						E-mail is empty.
					</c:when>
				</c:choose>
			</div>
		</c:if>
		<p>
			<label>E-mail address: <form:input path="email" /></label>
		</p>
		<p>
			<input type="submit" value="Get new password">
		</p>
	</form:form>
</jsp:attribute>
</us:page>