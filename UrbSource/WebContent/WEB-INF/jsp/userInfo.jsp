<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="us" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<us:page user="${user}">
<jsp:attribute name="head">
	<script>
	$(document).ready(function(){
			    var email = $('#email').val();
			    var gravatar_image_url = get_gravatar_image_url (email);
			    
			    
			    $('#image').attr('src', gravatar_image_url);
			});


	 function get_gravatar_image_url (email)
			{
			    return ("https://secure.gravatar.com/avatar/" + md5(email.toLowerCase().trim())+"?size=250");
			}
	</script>
	<script src="http://www.myersdaily.org/joseph/javascript/md5.js"></script>
</jsp:attribute>
<jsp:attribute name="mainPanel">
<h3 style="width:100%;padding-bottom:10px;border-bottom:1px dashed #333;margin-bottom:30px">User Information</h3>
<img id="image" style="float:left;margin-left:5%"/>
<c:choose>
<c:when test="${user.id == command.id}">
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
		<div style="margin-right:5%;float:right">
		<form:hidden path="username" />
		<p>
			<label>E-mail address<br> <form:input path="email" /></label>
		</p>
		<p>
			<label>First Name<br> <form:input path="firstName" /></label>
		</p>
		<p>
			<label>Last Name<br> <form:input path="lastName" /></label>
		</p>
		<p>
			<label>Password<br> <form:password path="password" /></label>
		</p>
		<p>
			<label>Password (again)<br> <form:password path="password2" /></label>
		</p>
		<p>
			Karma Points: ${user.karma}
		</p><!--
		<p>
			Experience Points: ${user.experiencePoints}
		</p>
		<p>
			Comment Points: ${user.commentPoints}
		</p>-->
		<p>
			<a href="/UrbSource/experience/user/${user.id}">You have shared ${user.numberOfExperiences} experiences</a>
		</p>
		<p>
			<input type="submit" value="Update Information" class="btn btn-primary">
		</p>
	</form:form>
	  </div>
</c:when>
<c:otherwise>
       <div style="margin-right:5%;float:right">
		<p>
			<label>E-mail address: ${command.email}</label>
			<input id="email" type="hidden" value="${command.email}"></input>
		</p>
		<p>
			<label>First Name: ${command.firstName}</label>
		</p>
		<p>
			<label>Last Name: ${command.lastName}</label>
		</p>
		<p>
			Karma Points: ${command.karma}
		</p>
		<p>
			Experience Points: ${command.experiencePoints}
		</p>
		<p>
			Comment Points: ${command.commentPoints}
		</p>
		<p>
			<a href="/UrbSource/experience/user/${command.id}">${command.username} has shared ${command.numberOfExperiences} experiences</a>
		</p>
		</div>
</c:otherwise>
</c:choose>
</jsp:attribute>
</us:page>