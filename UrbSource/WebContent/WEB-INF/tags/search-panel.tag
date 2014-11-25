<%@ tag language="java" pageEncoding="UTF-8"%>
<div style="width:100%;height:40px;background-color:#d5d5d5" class="top"> </div>
<div class="searchPanel" style="width:700px;margin-top:40px">
<img style="position:absolute;left:0;right:0;margin-left:auto;margin-right:auto;top:20px"
		src="/UrbSource/resources/images/urb.png"></img> 
	<div data-ng-controller="searchExperienceCtrl" style="margin-top:20%">
				<div style="margin-top: 6%">

				<input type="radio" name="search" value="text" checked ng-click="selectBy('text')"> by Text
				<input type="radio" name="search" value="tag" ng-click="selectBy('tag')">by Tag
				<input type="radio" name="search" value="location" ng-click="selectBy('location')">by Location
		</div>
		<div style="margin-top: 2%;">
		<div ng-show ="by == 'tag'" class="padded-row"
			style="margin-top: 2%; width: 100%;">
			<angucomplete id="searchExpLogin"
				placeholder="You can seach an experience" pause="100"
				selectedobject="tag" url="searchPanel_searchTag" titlefield="name"
				minlength="2" inputclass="form-control form-control-small"
				matchclass="highlight" />
		</div>
		<div ng-show ="by=='text'" > <input style="margin-top: 2%; width: 100%;" type="text" ng-model="text"></div>
		<div ng-show ="by=='location'" style="margin-top: 2%; width: 100%;">Location araması</div>
		</div>
		 <img ng-click="searchExp()" style="cursor:pointer;margin-left:101%;width:33px;height:33px;margin-top:-50px;z-index:10000" src="/UrbSource/resources/images/search.jpg" />
	</div>
</div>