passepartoutApp.questions = angular.module('passepartoutApp.questions',['passepartoutApp.user'])

.factory('$Questions', function($http, $User, $Friends, $Account) {
	
	function $Questions(){
		this.questions=[];
		this.doneGettingData = false;
	}
	
	
	$Questions.prototype.isDataFromServerLoaded = function(){
		return this.doneGettingData;
	}

	$Questions.prototype.getQuestions = function(){
	
			$http({
				url : "localhost:8080/rest/question",
				method : "GET",
				params : {
					user_id : $User.user_id
				}
			}).success((function(data) {

				var questions_data = data;
				this.questions=[];
		        var response = questions_data['response'];
		        for (index = 0; index < response.length; ++index) {
		        	question = JSON.parse(response[index]);

		            current_question = {
		            		questionText : JSON.parse(question['questionText']),
		            		options : JSON.parse(question['options']),
		            		answerIndex : JSON.parse(question['answerIndex'])
		                  };

		                  this.questions.push(current_question);
		                };
		            	this.doneGettingData=true;
			}).bind(this))}

	return new 	$Questions()
});


passepartoutApp.user = angular.module('passepartoutApp.user',[])

.factory('$User', function($http) {
	
	function $User(){
		this.user_id=-1;
		this.name;
		this.score;
		this.doneGettingData = false;
	}
	
	
	$User.prototype.isDataFromServerLoaded = function(){
		return this.doneGettingData;
	}

	$User.prototype.getUserId = function(){
	
			$http({
				url : "localhost:8080/rest/user/login",
				method : "GET",
				params : {
					user_name : $User.name
				}
			}).success((function(data) {

				this.id = data['reponse']['user_id'];
				this.doneGettingData=true;
			}).bind(this))}
	
	$User.prototype.setHighscore = function(){
		
		$http({
			url : "localhost:8080/rest/user/highscore",
			method : "POST",
			params : {
				user_id : $User.id,
				user_score : this.score
			}
		}).success((function(data) {

			this.doneGettingData=true;
		}).bind(this))}
	
	
	passepartoutApp.highscores = angular.module('passepartoutApp.highscore',['passepartoutApp.user'])

	.factory('$Highscores', function($http, $User) {
		
		function $Highscores(){
			this.highscores=[];
			this.doneGettingData = false;
		}
		
		
		$Highscores.prototype.isDataFromServerLoaded = function(){
			return this.doneGettingData;
		}

		$Highscores.prototype.getHighscores = function(){
		
				$http({
					url : "localhost:8080/rest/highscore",
					method : "GET",
					params : {
					}
				}).success((function(data) {

					var highscores_data = data;
					this.highscores=[];
			        var response = highscoes_data['response'];
			        for (index = 0; index < response.length; ++index) {
			        	score = JSON.parse(response[index]);

			            current_score = {
			            		user_id : JSON.parse(score['user_id']),
			            		score : JSON.parse(question['score'])
			                  };

			                  this.highscores.push(current_score);
			                };
			            	this.doneGettingData=true;
				}).bind(this))}

	return new 	$User()
});
