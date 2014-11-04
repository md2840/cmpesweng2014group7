<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="us" tagdir="/WEB-INF/tags" %>
<us:page user="${user}">
<jsp:attribute name="mainPanel">
	<div class="alert alert-success">
	 Your password has been reseted. A new password has been sent to your e-mail. Now you can <a href="/UrbSource/">login</a> with your new password. 
	</div>
</jsp:attribute>
</us:page>