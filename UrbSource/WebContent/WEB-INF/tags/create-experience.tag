<%@ tag language="java" pageEncoding="UTF-8"%>
<div id="create-experience-panel" class="panel panel-primary">
	<div class="panel-heading" style="cursor: pointer;z-index:9999" data-toggle="collapse" data-target="#collapseOne"> 
    <h4 class="panel-title">
	<a class="accordion-toggle">
          Click to share a new experience:
        </a>
        </h4>
        </div>
	<div id="collapseOne" class="panel-collapse collapse">
	<div class="panel-body">
	<p>
	<label for="experience-text">Tell us about your experience:</label><br/>
	<textarea name="experience-text" id="experience-text" style="width:100%" rows="10" maxlength="10000"></textarea>
	</p>
	<small><em>Experience text can be at most 10,000 characters.</em></small>
	<p>
	<label for="experience-tags">Tags:</label><br>
	<input type="text" name="experience-tags" id="experience-tags" />
	</p>
	<small><em>You can enter multiple tags by separating them with commas (for example: "traffic, Silivri").</em></small>
	<p>
	<label for="experience-tags">Mood:</label><br>
	<select id="experience-mood" name="experience-mood">
		<option value="good" selected>Good</option>
		<option value="bad">Bad</option>
		<option>Neutral</option>
		<option>Angry</option>
		<option>Confused</option>
		<option>Safe</option>
		<option>Afraid</option>
		<option>Annoyed</option>
	</select>
	</p>
	<p>
		<label for="experience-date">When Experience Happened?</label><br>
		<input type="date" name="experience-date" id="experience-date">
	</p>
	<p>
		<label for="experience-date">Expiration Date</label><br>
		<input type="date" name="expires-date" id="expires-date">
	</p>
	<small><em>Leave expiration date empty if experience is permanent.</em></small><br>
	<label for="experience-locations">Location: (Click on the place where the experience has happened.)</label><br>
	<input id="pac-input" class="controls" type="text" placeholder="Search Box">
  	<div id="map-canvas" style="left:0;right:0;margin-left:auto;margin-right:auto;margin-bottom:2em;height:300px;top:20px"></div>
  	<div id="latbox"></div>
  	<div id="lngbox"></div>
	<div class="panel-footer">
		<button type="button" class="btn btn-primary" id="create-experience">Share!</button>
	</div>
	</div>
	</div>
</div>