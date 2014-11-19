<%@ tag import="net.tanesha.recaptcha.ReCaptcha" %>
<%@ tag import="net.tanesha.recaptcha.ReCaptchaFactory" %>


<%
	ReCaptcha reCaptcha = ReCaptchaFactory.newSecureReCaptcha("6LejB_0SAAAAAHkLlwmECsILMP--s1JRmnYdmu8j", "6LejB_0SAAAAAIpYFg8i2CUNDuAIXnBNPRw5QRQv", false);
	out.print(reCaptcha.createRecaptchaHtml(null, null));
%>