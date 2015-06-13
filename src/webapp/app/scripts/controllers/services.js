var passepartoutApp = angular.module('passepartoutApp');
var baseApiLocation = "http://localhost:8080/rest/"

passepartoutApp.questions = angular.module('passepartoutApp.questions',['passepartoutApp.user'])

.factory('$Questions', function($http) {

	function $Questions(){
		this.questions=[];
	}

	$Questions.prototype.getQuestions = function(id, callback){

		$http({
			url : baseApiLocation + "question",
			method : "GET",
			params : {
				userId : id
			}
		}).success((function(questions_data) {
		
			//var questions_data = data['questions'];
			this.questions= questions_data;
		
			callback();
//			for (index = 0; index < questions_data.length; ++index) {
//				question = JSON.parse(questions_data[index]);
//
//				current_question = {
//						questionText : JSON.parse(question['questionText']),
//						options : JSON.parse(question['options']),
//						answerIndex : JSON.parse(question['answerIndex'])
//				};
//
//				this.questions.push(current_question);
//			};
		
		}).bind(this))}

	return new 	$Questions()
});


passepartoutApp.user = angular.module('passepartoutApp.user',[])

.factory('$User', function($http, $Questions) {

	function $User(){
		this.id=-1;
		this.name;
	
		
	}


	$User.prototype.getUserId = function(callback){

		$http({
			url : baseApiLocation + "user/login",
			method : "GET",
			params : {
				name : this.name
			}
		}).success((function(user) {
			console.log(user);
			this.id = user['id'];
			console.log(this.id);
			console.log(this.name);
			$Questions.getQuestions(this.id, callback);
			
		}).bind(this))}

	$User.prototype.setHighscore = function(user_score){

		$http({
			url : baseApiLocation + "highscore",
			method : "POST",
			headers : {
				'Content-Type' : 'application/x-www-form-urlencoded',
			},
			data : {
				user_id : this.id,
				score : user_score
			}
		}).success((function(data) {

	
		}).bind(this));
	}

	
	return new 	$User()
});

passepartoutApp.highscores = angular.module('passepartoutApp.highscores',['passepartoutApp.user'])

.factory('$Highscores', function($http, $User) {

	function $Highscores(){
		this.highscores=[];
	}


	$Highscores.prototype.getHighscores = function(){

		$http({
			url : baseApiLocation + "highscore/top",
			method : "GET",
			params : {
			}
		}).success((function(data) {

			this.highscores= data;
			
	
		}).bind(this))}

	return new $Highscores()
});

