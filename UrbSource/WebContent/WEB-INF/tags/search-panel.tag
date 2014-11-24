<%@ tag language="java" pageEncoding="UTF-8"%>
<div class="searchPanel">
	<div data-ng-controller="searchExperienceCtrl">
				<div style="margin-top: 6%">

				<input type="radio" name="search" value="text" checked ng-click="selectBy('text')"> by Text
				<input type="radio" name="search" value="tag" ng-click="selectBy('tag')">by Tag
				<input type="radio" name="search" value="location" ng-click="selectBy('location')">by Location
		</div>
		<div style="margin-top: 2%; margin-left: 2%; width: 300%;">
		<div ng-show ="by == 'tag'" class="padded-row"
			style="margin-top: 2%; margin-left: 2%; width: 85%;">
			<angucomplete id="searchExpLogin"
				placeholder="You can seach an experience" pause="100"
				selectedobject="tag" url="searchPanel_searchTag" titlefield="name"
				minlength="2" inputclass="form-control form-control-small"
				matchclass="highlight" />
		</div>
		<div ng-show ="by=='text'" > <input style="margin-top: 2%; margin-left: 2%; width: 85%;" type="text" ng-model="text"></div>
		<div ng-show ="by=='location'" style="margin-top: 2%; margin-left: 2%; width: 85%;">Location araması</div>
		</div>
		<button type="button"
			style="margin-top: -12%; margin-left: 300%; width: 47px; display: flex;"
			ng-click="searchExp()">
			<img style="width: 47px;" src="resources/images/search.jpg" />
		</button>
	</div>
</div>