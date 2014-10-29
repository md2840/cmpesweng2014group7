<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="us" tagdir="/WEB-INF/tags" %>
<%@ attribute name="user" required="true" %>
<div class="userInfoPanel">
<sec:authorize access="isAnonymous()">
	<h3>
		Welcome to UrbSource!
	</h3>
	<us:login></us:login>
</sec:authorize>
<sec:authorize access="isAuthenticated()">
	<h3>
		Welcome ${user.firstName} ${user.lastName}.
	</h3>
	<p>
		Your experience point i 0.
	</p>
	<p>
	<c:url var="logoutUrl" value="j_spring_security_logout"/>
	<form action="${logoutUrl}" method="post">
		<input type="submit" value="Log out"  class="btn btn-danger"/>
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	</form>
</sec:authorize>
</div>