<%@ tag language="java" pageEncoding="UTF-8"%>
<div class="searchPanel">
	<img  style="width:700px; height:175px" src="/UrbSource/resources/images/cityoflight-photoshop.png"></img>
	<div data-ng-controller="searchExperienceCtrl">
	<div class="padded-row" style="margin-top:60px; margin-left:21px">
		<angucomplete id="searchExp" placeholder="You can seach an experience"
					 pause="100" selectedobject="tag" url="searchPanel_searchName" titlefield="N_NAME" minlength="3" inputclass="form-control form-control-small"
							matchclass="highlight">
	</div>
	</div>
</div>
