<%@ tag language="java" pageEncoding="UTF-8"%>
<div class="searchPanel">
	<a href="/UrbSource/Index/"><img  style="width:700px; height:175px" src="/UrbSource/resources/images/cityoflight-photoshop.png"></a>
	<div data-ng-controller="searchExperienceCtrl">
		<button type="button" style="margin-top:57px;margin-left:610px;width:47px;display:flex;" ng-click="searchExp()"><img style="width:47px;" src="/UrbSource/resources/images/search.jpg"/></button>
		<div class="padded-row" style="margin-top: -34px; margin-left: 21px; width:600px;">
			<angucomplete id="searchExpLogin" 
				text = "text"
				placeholder="You can seach an experience" pause="100"
				selectedobject="tag" url="searchPanel_searchTag"
				titlefield="name" minlength="2"
				inputclass="form-control form-control-small" matchclass="highlight" />
		</div>
	</div>
</div>