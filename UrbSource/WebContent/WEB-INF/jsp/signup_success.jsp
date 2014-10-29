<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="us" tagdir="/WEB-INF/tags" %>
<us:page user="${user}">
<jsp:attribute name="mainPanel">
	<div class="alert alert-success">
	You have been successfully registered. Now you can <a href="/UrbSource/">login.</a>
	</div>
</jsp:attribute>
</us:page>