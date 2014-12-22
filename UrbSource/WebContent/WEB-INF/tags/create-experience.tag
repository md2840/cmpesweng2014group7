<%@ tag language="java" pageEncoding="UTF-8"%>
<div id="create-experience-panel" class="panel panel-primary">
	<div class="panel-heading">Share a new experience:</div>
	<div class="panel-body">
	<p>
	<label for="experience-text">Tell us about your experience:</label><br/>
	<textarea name="experience-text" id="experience-text" style="width:100%"></textarea>
	</p>
	<p>
	<label for="experience-tags">Tags:</label><br>
	<input type="text" name="experience-tags" id="experience-tags" />
	</p>
	<p>
	<label for="experience-tags">Mood:</label><br>
	<select id="experience-mood" name="experience-mood">
		<option value="good" selected>Good</option>
		<option value="bad">Bad</option>
	</select>
	</p>
	<label for="experience-locations">Location:</label><br>
	<input id="pac-input" class="controls" type="text" placeholder="Search Box">
  	<div id="map-canvas" style="left:0;right:0;margin-left:auto;margin-right:auto;margin-bottom:2em;height:300px;top:20px"></div>
	</div>
	<div class="panel-footer">
		<button type="button" class="btn btn-primary" id="create-experience">Share!</button>
	</div>
</div>