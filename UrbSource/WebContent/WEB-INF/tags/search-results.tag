<%@ tag language="java" pageEncoding="UTF-8"%>
<div ng-show="search">
	Search Results:<br>
	<div id="experience-list">
		<div class="panel panel-default"
			ng-repeat="experience in experienceList">
			<div class="panel-heading">
				<span ng-switch on="experience.mood">
					<span ng-switch-when="good" style="color: green">Good Experience</span>
					<span ng-switch-when="bad" style="color: red">Bad Experience</span>
					<span ng-switch-default></span>
				</span>
				by <a href="/UrbSource/user/info/{{experience.author.id}}">{{experience.author.username}}</a>
					on {{ experience.creationTime | date:'yyyy-MM-dd' }}<span ng-if="experience.expirationDate != null && experience.expirationDate != undefined && experience.expirationDate"> - valid until {{ experience.expirationDate | date:'yyyy-MM-dd' }}</span>
			</div>
			<div class="{{experience.source}}"></div>
			<div class="panel-body">
				<p>
					<p>{{experience.text}}</p>
					<button style="float:right;display:none;" ng-click="save(experience.id,$event)" type="button" class="btn btn-primary btn-xs">Save</button>
				</p>
				<p>
					Tags: <span>{{experience.tagNames}}</span>
				</p>
			</div>
			
			<div class="panel-footer clearfix">
				<div class="pull-left">
					<div class="btn-toolbar" style="display: inline-block !important;">
						<div class="btn-group" style="display: inline-block !important;">
							<button type="button" ng-click="upvote(experience.id, $event)" class="btn btn-xs btn-primary glyphicon glyphicon-thumbs-up" ng-if="experience.upvotedByUser"></button>
							<button type="button" ng-click="upvote(experience.id, $event)" class="btn btn-xs btn-default glyphicon glyphicon-thumbs-up" ng-if="!experience.upvotedByUser"></button>
							<button type="button" ng-click="downvote(experience.id, $event)" class="btn btn-xs btn-primary glyphicon glyphicon-thumbs-down" ng-if="experience.downvotedByUser"></button>
							<button type="button" ng-click="downvote(experience.id, $event)" class="btn btn-xs btn-default glyphicon glyphicon-thumbs-down" ng-if="!experience.downvotedByUser"></button>
						</div>
					</div>
					~ {{experience.points}} Points -
					<a href="/UrbSource/experience/id/{{experience.id}}">{{experience.numberOfComments}} Comments</a>
				</div>
				<div class="btn-toolbar pull-right">
					<div class="btn-group" ng-show="experience.author.username === '${user.username}'">
						<button type="button" class="btn btn-primary btn-xs" onclick="editExp(this)" id="edit-experience">Edit</button>
						<button type="button" class="btn btn-primary btn-xs" ng-click="deleteExp(experience.id)" id="delete-experience">Delete</button>
					</div>
					<div class="btn-group" ng-hide="experience.userMarkedSpam">
						<button type="button" class="btn btn-danger btn-xs" ng-click="reportSpam(experience.id, $event)" id="report-spam">Report Spam</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>