<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="us" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ attribute name="mainPanel" fragment="true" required="true" %>
<%@ attribute name="head" fragment="true" required="false" %>
<%@ attribute name="user" required="true" type="com.urbsource.models.User" %>
<!DOCTYPE html>
<html data-ng-app="urbsourceLogin">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>UrbSource</title>
<!-- RESOURCES -->

<!-- JS -->
<script src="/UrbSource/resources/jquery-1.11.1.js"></script>
<script src="/UrbSource/resources/angular/angular.js"></script>
<script src="/UrbSource/resources/angular/ui-bootstrap-tpls-0.11.0.js"></script>
<script src="/UrbSource/resources/login/angucomplete.js"></script>
<script src="/UrbSource/resources/js/ng-grid.debug.js"></script>
<script src="/UrbSource/resources/js/toastr.js"></script>

<!--  CSS -->
<link rel="stylesheet" type="text/css" href="/UrbSource/resources/css/ng-grid.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css"
	href="/UrbSource/resources/css/angucomplete.css">
<link rel="stylesheet" type="text/css"
	href="/UrbSource/resources/css/urbSource.css">
<link rel="stylesheet" type="text/css"
	href="/UrbSource/resources/css/toastr.css">
	
<!-- APP -->
<script src="/UrbSource/app/login/app.js"></script>
<script src="/UrbSource/app/login/util/AngucompleteService.js"></script>
<!-- MAIN PAGE -->
<script src="/UrbSource/app/login/mainPage/js/directives.js"></script>
<script src="/UrbSource/app/login/mainPage/js/controllers.js"></script>
<script src="/UrbSource/app/login/mainPage/mainPanel/controllers.js"></script>
<script src="/UrbSource/app/login/mainPage/searchPanel/controllers.js"></script>
<!-- Search Experience Service -->
<script src="/UrbSource/app/login/mainPage/searchPanel/services.js"></script>
<sec:authorize access="isAuthenticated()">
	<script src="/UrbSource/app/createExperience.js"></script>
	<script src="/UrbSource/app/voteExperience.js"></script>
</sec:authorize>

<style>
	#map-canvas {
		height: 100%;
		margin: 0px;
		padding: 0px
    }
    .controls {
		margin-top: 16px;
		border: 1px solid transparent;
		border-radius: 2px 0 0 2px;
		box-sizing: border-box;
		-moz-box-sizing: border-box;
		height: 32px;
		outline: none;
		box-shadow: 0 2px 6px rgba(0, 0, 0, 0.3);
    }

    #pac-input {
		background-color: #fff;
		padding: 0 11px 0 13px;
		width: 400px;
		font-family: Roboto;
		font-size: 15px;
		font-weight: 300;
		text-overflow: ellipsis;
    }

    #pac-input:focus {
		border-color: #4d90fe;
		margin-left: -1px;
		padding-left: 14px;  /* Regular padding-left + 1. */
		width: 401px;
    }

    .pac-container {
		font-family: Roboto;
    }

    #type-selector {
		color: #fff;
		background-color: #4d90fe;
		padding: 5px 11px 0px 11px;
    }

    #type-selector label {
		font-family: Roboto;
		font-size: 13px;
		font-weight: 300;
    }
    
    .popular::before {
		content: "Popular";
	}
	
    .recent::before {
		content: "Recent";
	}
	
	.popular::before, .recent::before {
		padding: 2px 7px;
		font-size: 12px;
		font-weight: bold;
		background-color: whiteSmoke;
		border: 1px solid #DDD;
		color: #9DA0A4;
		-webkit-border-radius: 0 0 4px 0;
		-moz-border-radius: 0 0 4px 0;
		border-radius: 0 0 4px 0;
	}
</style>
<script src="https://maps.googleapis.com/maps/api/js?v=3.exp&libraries=places"></script>
<script>

function initialize() { 
  var markers = [];
  var map = new google.maps.Map(document.getElementById('map-canvas'), {
	zoom: 11,
	center: new google.maps.LatLng(41.018990, 29.001056),
    mapTypeId: google.maps.MapTypeId.ROADMAP
  });

  var defaultBounds = new google.maps.LatLngBounds(
      new google.maps.LatLng(40.793827, 28.301845),
      new google.maps.LatLng(41.285832, 29.838557));
 

  // Create the search box and link it to the UI element.
  var input = /** @type {HTMLInputElement} */(
      document.getElementById('pac-input'));
  map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);
  var searchBox = new google.maps.places.SearchBox(
    /** @type {HTMLInputElement} */(input));

  // [START region_getplaces]
  // Listen for the event fired when the user selects an item from the
  // pick list. Retrieve the matching places for that item.
  google.maps.event.addListener(searchBox, 'places_changed', function() {
    var places = searchBox.getPlaces();
    if (places.length == 0) {
      return;
    }
    for (var i = 0, marker; marker = markers[i]; i++) {
      marker.setMap(null);
    }

    // For each place, get the icon, place name, and location.
    markers = [];
    var bounds = new google.maps.LatLngBounds();
    for (var i = 0, place; place = places[i]; i++) {
      var image = {
        url: place.icon,
        size: new google.maps.Size(71, 71),
        origin: new google.maps.Point(0, 0),
        anchor: new google.maps.Point(17, 34),
        scaledSize: new google.maps.Size(25, 25)
      };

      // Create a marker for each place.
      var marker = new google.maps.Marker({
        map: map,
        icon: image,
        title: place.name,
        position: place.geometry.location
      });

      markers.push(marker);

      bounds.extend(place.geometry.location);
    }

    map.fitBounds(bounds);
  });
  // [END region_getplaces]

  // Bias the SearchBox results towards places that are within the bounds of the
  // current map's viewport.
  google.maps.event.addListener(map, 'bounds_changed', function() {
    var bounds = map.getBounds();
    searchBox.setBounds(bounds);
  });
}

google.maps.event.addDomListener(window, 'load', initialize);

Date.prototype.toDateInputValue = (function() {
    var local = new Date(this);
    local.setMinutes(this.getMinutes() - this.getTimezoneOffset());
    return local.toJSON().slice(0,10);
});

jQuery(function($) {
	$("#experience-date").each(function() {
		if ($(this).val() === '')
			$(this).val(new Date().toDateInputValue());
	});
});
</script>
 <style>
      #target {
        width: 345px;
      }
</style>

<jsp:invoke fragment="head" />
</head>
<body>
    <div id="test"></div>
	<div class="containSearchLogin" ng-controller="URLCtrlLogin">
		<us:search-panel></us:search-panel>
		<us:user-info user="${user}"></us:user-info>
	</div>
	<div class="mainPanel">
		<sec:authorize access="isAuthenticated()">
			<us:create-experience></us:create-experience>
		</sec:authorize>
		<jsp:invoke fragment="mainPanel"/>
	</div>
</body>
</html>