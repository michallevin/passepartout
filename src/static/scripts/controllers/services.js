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
			this.questions= questions_data;
			callback();	
		}).bind(this));
	}
	return new $Questions()
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

	$User.prototype.setHighscore = function(user_score, callback){
		$http({
		    method: 'POST',
		    url: baseApiLocation + "highscore",
		    data: "user_id=" + this.id + "&score=" + user_score,
		    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
		})
		.success((function(data) {
			callback();
		}).bind(this));
	}
	return new $User();
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

	return new $Highscores();
});


passepartoutApp.manage = angular.module('passepartoutApp.manage', [])

.factory('$Admin', function($http) {

	function $Admin() { }

	$Admin.prototype.startImport = function(callback){

		$http({
			url : "http://localhost:8080/import/start",
			method : "GET",
			params : {
			}
		}).success((function(data) {			
			callback();
		}).bind(this))}

	$Admin.prototype.cancelImport = function(callback){

		$http({
			url : "http://localhost:8080/import/cancel",
			method : "GET",
			params : {
			}
		}).success((function(data) {			
			callback();
		}).bind(this))}

	$Admin.prototype.getImportUpdate = function(callback){

		$http({
			url : "http://localhost:8080/import/status",
			method : "GET",
			params : {
			}
		}).success((function(data) {			
			callback(data);
		}).bind(this))}

	return new $Admin();
});

