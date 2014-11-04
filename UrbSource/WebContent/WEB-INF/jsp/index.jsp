<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="us" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<us:page user="${user}">
<jsp:attribute name="head">
  <script>

    $(document).ready(function() {
    	toastr.options = {
    			  "closeButton": false,
    			  "debug": false,
    			  "positionClass": "toast-bottom-right",
    			  "onclick": null,
    			  "showDuration": "3000",
    			  "hideDuration": "10000",
    			  "timeOut": "50000",
    			  "extendedTimeOut": "10000",
    			  "showEasing": "swing",
    			  "hideEasing": "linear",
    			  "showMethod": "fadeIn",
    			  "hideMethod": "fadeOut"
    			};
    	toastr.info("This is a great city! I would love to come again.", "Mustafa Demirel");
    	toastr.info("Best Iskender Doner I've ever had.", "Steve Jobs");
    	toastr.info("Traffic is so frustrating.", "Ilgın Yasar");

    });
 </script> 
</jsp:attribute>
<jsp:attribute name="mainPanel">
	Recent Experiences:
</jsp:attribute>
</us:page>