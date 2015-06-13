var passepartoutApp = angular.module('passepartoutApp');
var baseApiLocation = "http://localhost:8080/rest/"

passepartoutApp.questions = angular.module('passepartoutApp.questions',['passepartoutApp.user'])

.factory('$Questions', function($http) {

	function $Questions(){
		this.questions=[];
	}

	$Questions.prototype.getQuestions = function(id, callback){

		//this.questions= [{"questionText":"Which of these happend in <Belgium>?","options":["<Battle_of_Steenkerque>","<Battle_of_Belvoir_Castle>","<Los_Alfaques_disaster>","<Battle_of_Monte_Cassino>"],"answerIndex":0,"score":100,"posterImage":"belgium2.jpg","label":"Belgium"},{"questionText":"Which of these people lives/lived in <France>?","options":["<Yuuki_Kondo>","<Bob_Cashell>","<William_Lawrence_Kocay>","<Maurice_L\\u00E9vy_(Publicis)>"],"answerIndex":3,"score":100,"posterImage":"paris1.jpg","label":"France"},{"questionText":"Which of these happend in <Germany>?","options":["<2000\\u201301_UEFA_Cup>","<Battle_of_the_Saintes>","<Operation_Tiger_Hound>","<Battle_of_Latakia>"],"answerIndex":0,"score":100,"posterImage":"germany1.jpg","label":"Germany"},{"questionText":"Who is the leader of <Liechtenstein>?","options":["<Abdulla_Shahid>","<Adrian_Hasler>","<Park_Geun-hye>","<Abdelilah_Benkirane>"],"answerIndex":1,"score":200,"posterImage":"lichtenstien1.jpg","label":"Liechtenstein"},{"questionText":"Which of these people lives/lived in <Italy>?","options":["<Judit_Varga>","<Polona_Hercog>","<Ernst_Hirsch_Ballin>","<Duncan_MacDougall_(doctor)>"],"answerIndex":0,"score":200,"posterImage":"italy1.jpg","label":"Italy"},{"questionText":"Which of the following is the a state in <Greece>?","options":["<Kyoto>","<City_of_San_Marino>","<Veria>","<Semmering_railway>"],"answerIndex":2,"score":200,"posterImage":"greece1.jpg","label":"Greece"},{"questionText":"Who was born in <Turkey>?","options":["<Daniel_Beahan>","<Derrick_Helton>","<\\u0130pek_Soro\\u011Flu>","<Olesya_Vladykina>"],"answerIndex":2,"score":300,"posterImage":"turkey4.jpg","label":"Turkey"},{"questionText":"Which company is located in <Egypt>?","options":["<Snyder's_of_Hanover>","<Elbit_Systems>","<EUC_Construction_\\u2013_El_Hazek>","<Banco_Ita\\u00FA_Paraguay>"],"answerIndex":2,"score":300,"posterImage":"egypt3.jpg","label":"Egypt"},{"questionText":"Who was born in <Yemen>?","options":["<Lee_Balkin>","<Christine_Babcock>","<Robert_Landers>","<Mohammed_Abdel_Karim_Al_Ghezali>"],"answerIndex":3,"score":300,"posterImage":"yemen1.jpg","label":"Yemen"},{"questionText":"Which of the following has its origin in <India>?","options":["<Space_UK>","<TEMA_(group)>","<Waysted>","<K-4_(SLBM)>"],"answerIndex":3,"score":400,"posterImage":"india1.jpg","label":"India"},{"questionText":"Who is the leader of <India>?","options":["<Khalifa_bin_Zayed_Al_Nahyan>","<Dmitry_Medvedev>","<Saulos_Chilima>","<Narendra_Modi>"],"answerIndex":3,"score":400,"posterImage":"india3.jpg","label":"India"},{"questionText":"Which of these people lives/lived in <Thailand>?","options":["<Simon_Yates_(golfer)>","<Koolade>","<Francesco_Molinari>","<Mimsy_Farmer>"],"answerIndex":0,"score":400,"posterImage":"thailand3.jpg","label":"Thailand"},{"questionText":"Who was born in <China>?","options":["<Wang_Xuwen_(professional_gamer)>","<Kim_Yong-shik>","<Alexander_Volzhin>","<Carolyn_Woods>"],"answerIndex":0,"score":500,"posterImage":"china2.jpg","label":"China"},{"questionText":"Which of these happend in <Japan>?","options":["<Doolittle_Raid>","<Battle_of_the_Neva>","<Al-Qaeda_insurgency_in_Yemen>","<pl/Bitwa_pod_Saguntem_(75_p.n.e.)>"],"answerIndex":0,"score":500,"posterImage":"japan1.jpg","label":"Japan"},{"questionText":"Which company is located in <United_States>?","options":["<Aeroplan>","<Netcraft>","<Avala_Film>","<Monster_Beverage>"],"answerIndex":3,"score":500,"posterImage":"san_fransisco1.jpg","label":"United States"},{"questionText":"Who was born in <United_States>?","options":["<Arnaldo_Carvalho_de_Melo>","<Peter_Rocca>","<Valerie_Woodbridge>","<Norma_Koplick>"],"answerIndex":1,"score":600,"posterImage":"new_york4.jpg","label":"United States"},{"questionText":"Who is/was a politician of <Republic_of_Ireland>?","options":["<Andrea_Nahles>","<Jim_Fitzsimons>","<Alain_Akouala_Atipault>","<de/Marek_Ma\\u010Fari\\u010D>"],"answerIndex":1,"score":600,"posterImage":"ireland2.jpg","label":"Republic of Ireland"},{"questionText":"Which of the following has its origin in <United_Kingdom>?","options":["<C\\u00E9sar_(grape)>","<The_Fourmyula>","<Luminary_(band)>","<The_Flirts>"],"answerIndex":2,"score":600,"posterImage":"london3.jpg","label":"United Kingdom"}];
		//callback();
		//return;

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

	
	return new 	$User();
});

passepartoutApp.highscores = angular.module('passepartoutApp.highscores',['passepartoutApp.user'])

.factory('$Highscores', function($http, $User) {

	function $Highscores(){
		this.highscores=[];
	}


	$Highscores.prototype.getHighscores = function(){

		$http({
			url : baseApiLocation + "highscore",
			method : "GET",
			params : {
			}
		}).success((function(data) {

			this.highscores= data;
			
	
		}).bind(this))}

	return new $Highscores();
});


// passepartoutApp.highscores = angular.module('passepartoutApp.admin')

// .factory('$Admin', function($http, $User) {

// 	function $Admin() { }

// 	$Admin.prototype.startImport = function(callback){

// 		$http({
// 			url : "http://localhost:8080/import/start",
// 			method : "GET",
// 			params : {
// 			}
// 		}).success((function(data) {			
// 			callback();
// 		}).bind(this))}

// 	$Admin.prototype.cancelImport = function(callback){

// 		$http({
// 			url : "http://localhost:8080/import/cancel",
// 			method : "GET",
// 			params : {
// 			}
// 		}).success((function(data) {			
// 			callback();
// 		}).bind(this))}

// 	$Admin.prototype.getImportUpdate = function(callback){

// 		$http({
// 			url : "http://localhost:8080/import/status",
// 			method : "GET",
// 			params : {
// 			}
// 		}).success((function(data) {			
// 			callback(data);
// 		}).bind(this))}

// 	return new $Admin();
// });

