<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="us" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="user" required="true" type="com.urbsource.models.User" %>
<div class="userInfoPanel">
<sec:authorize access="isAnonymous()">
	<h3>
		Welcome to UrbSource!
	</h3>
	<us:login></us:login>
</sec:authorize>
<sec:authorize access="isAuthenticated()">
	<h3>
		Welcome <a href="/UrbSource/user/info">${user.firstName} ${user.lastName}</a>
	</h3>
	<p>
	<c:url var="logoutUrl" value="/UrbSource/logout.html"/>
	<form action="${logoutUrl}" method="post">
		<input type="submit" value="Log out"  class="btn btn-danger"/>
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	</form>
</sec:authorize>
</div>