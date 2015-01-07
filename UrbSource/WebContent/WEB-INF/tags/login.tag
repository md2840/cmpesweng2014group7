<%@ tag language="java" pageEncoding="UTF-8"%>
<!-- @author Admir Nurkovic -->
<div style="margin-left: 100px; margin-top: 10px;"
	class="loginUserPanel">
	<form method="post" action="/UrbSource/j_spring_security_check">
		<div id="passwordLoginOption" class="form">
			<div class="row">
				<div class="label left">
					<label for="j_username">Username:</label>
				</div>
				<div class="right">
					<div class="textWrapper">
						<input type="text" name="j_username" />
					</div>
				</div>
				<div class="cl"></div>
			</div>
			<div class="row">
				<div class="label left">
					<label for="j_password">Password:</label>
				</div>
				<div class="right">
					<div class="textWrapper">
						<input type="password" name="j_password" />
					</div>
				</div>
				<div class="cl"></div>
			</div>
			<div class="buttons">
				<input style="margin-top: 1%" type="submit" class="btn btn-primary"
					value="Login" />
			</div>
		</div>
	</form>
	<form action="/UrbSource/signup/confirm" method="GET">
		<div style="margin-top: 1%" class="buttons">
			<input type="submit" class="btn btn-primary"
				value="sign up!" />
		</div>
	</form>
	<form action="/UrbSource/user/forgot" method="GET">
		<div style="margin-top: 1%" class="buttons">
			<input type="submit" class="btn btn-primary"
				value="Forgot my password" />
		</div>
	</form>

</div>