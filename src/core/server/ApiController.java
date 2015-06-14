package core.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import parsing.YagoImport;
import core.GameConfigInit;
import core.Question;
import db.models.Country;
import db.models.CountryOrder;
import db.models.FactType;
import db.models.Fact;
import db.models.FactTypeQuestionWording;
import db.models.Highscore;
import db.models.User;
import db.models.UserFactHistory;


@RestController
public class ApiController {

	@RequestMapping(value="/rest/question", method=RequestMethod.GET)
	public List<Question> getQuestions(@RequestParam("userId") Integer userId) {

		int literalRandomStart = new Random().nextInt(2);
		List<Country> countries = Country.fetchByOrder();
		List<Question> questions = new ArrayList<Question>();
		int i = 0;
		for (Country country : countries) {
			Question question = Question.generateQuestion(country, userId, i % 2 == literalRandomStart, i + 1 );
			question.setScore((int) ((Math.floor(i/3)+1)*100));
			question.setPosterImage(country.getPosterImage());
			question.setLabel(country.getRouteLabel());
			questions.add(question);
			i += 1;
		}

		return questions;
	}


	/* import */ 

	@RequestMapping(value="/import/start", method=RequestMethod.GET)
	public void startImport() {
		Thread t = new Thread() {
			public void run() {
				if (!YagoImport.isImporting()) {
					YagoImport.startImport(
							new String[] {
									"links", 
									"countries", 
									"attributes", 
									"facts", 
									"literalFacts", 
									"labels"
							});
					
					GameConfigInit.setCountryOrder();
					GameConfigInit.setQuestionWordings();

				}
			}
		};
		t.start();
	}
	@RequestMapping(value="/import/cancel", method=RequestMethod.GET)
	public void cancelImport() {
		YagoImport.cancelImport();
	}

	@RequestMapping(value="/import/status", method=RequestMethod.GET)
	public boolean getImportStatus() {
		return YagoImport.isImporting();
	}

	/* CRUD */ 

	// country

	@RequestMapping(value="/rest/country", method=RequestMethod.GET)
	public List<Country> getCountries() {
		List<Country> countries = Country.fetchAll();
		return countries;
	}

	@RequestMapping(value="/rest/country", method=RequestMethod.POST)
	public Country addCountry(@RequestParam("name") String name, @RequestParam("label") String label) {
		Country country = new Country(-1, "", name, label, false);
		country.save();
		return country;

	}

	@RequestMapping(value="/rest/country/{id}", method=RequestMethod.GET)
	public Country getCountry(@PathVariable Integer id) {
		return Country.fetchById(id);
	}


	@RequestMapping(value="/rest/country/{id}", method=RequestMethod.PUT)
	public Country editCountry(@PathVariable Integer id, @RequestParam("name") String name,  @RequestParam("label") String label) {
		Country country = Country.fetchById(id);
		country.setName(name);
		country.setLabel(label);
		country.update();
		return country;
	}

	@RequestMapping(value="/rest/country/{id}", method=RequestMethod.DELETE)
	public Country deleteCountry(@PathVariable Integer id) {
		Country country = Country.fetchById(id);
		country.delete();
		return country;
	}

	// highscore

	@RequestMapping(value="/rest/highscore", method=RequestMethod.GET)
	public List<Highscore> getHighScores() {
		return Highscore.fetchAll();
	}

	@RequestMapping(value="/rest/highscore/top", method=RequestMethod.GET)
	public List<Highscore> getTopHighScores() {
		return Highscore.fetchTop(10);
	}

	@RequestMapping(value="/rest/highscore", method=RequestMethod.POST)
	public Highscore addHighScore(@RequestParam("user_id") Integer userId, @RequestParam("score") Integer score) {
		Highscore highscore = new Highscore(userId, score);
		highscore.save();
		return highscore;
	}

	@RequestMapping(value="/rest/highscore/{id}", method=RequestMethod.GET)
	public Highscore getHighscore(@PathVariable Integer id) {
		return Highscore.fetchById(id);
	}

	@RequestMapping(value="/rest/highscore/{id}", method=RequestMethod.PUT)
	public Highscore editHighscore(@PathVariable Integer id,@RequestParam("user_id") Integer userId, @RequestParam("score") Integer score) {
		Highscore highscore = Highscore.fetchById(id);
		highscore.setScore(score);
		highscore.setUserId(userId);
		highscore.update();
		return highscore;
	}

	@RequestMapping(value="/rest/highscore/{id}", method=RequestMethod.DELETE)
	public Highscore deleteHighscore(@PathVariable Integer id) {
		Highscore highscore = Highscore.fetchById(id);
		highscore.delete();
		return highscore;
	}

	//fact type

	@RequestMapping(value="/rest/fact_type", method=RequestMethod.GET)
	public List<FactType> getFactTypes() {
		List<FactType> factTypes = FactType.fetchAll();
		return factTypes;
	}

	@RequestMapping(value="/rest/fact_type", method=RequestMethod.POST)
	public FactType addFactType(@RequestParam("name") String name, @RequestParam("is_literal") Boolean isLiteral) {
		FactType factType = new FactType(name, isLiteral);
		factType.save();
		return factType;

	}

	@RequestMapping(value="/rest/fact_type/{id}", method=RequestMethod.GET)
	public FactType getFactType(@PathVariable Integer id) {
		return FactType.fetchById(id);
	}


	@RequestMapping(value="/rest/fact_type/{id}", method=RequestMethod.PUT)
	public FactType editFactType(@PathVariable Integer id, @RequestParam("name") String name) {
		FactType factType = FactType.fetchById(id);
		factType.setTypeName(name);
		factType.update();
		return factType;
	}

	@RequestMapping(value="/rest/fact_type/{id}", method=RequestMethod.DELETE)
	public FactType deleteFactType(@PathVariable Integer id) {
		FactType factType = FactType.fetchById(id);
		factType.delete();
		return factType;
	}



	// user

	@RequestMapping(value="/rest/user", method=RequestMethod.GET)
	public List<User> getUser() {
		List<User> users = User.fetchAll();
		return users;
	}

	@RequestMapping(value="/rest/user", method=RequestMethod.POST)
	public User addUser(@RequestParam("name") String name) {
		User user = new User(name);
		user.save();
		return user;

	}

	@RequestMapping(value="/rest/user/{id}", method=RequestMethod.GET)
	public User getUser(@PathVariable Integer id) {
		return User.fetchById(id);
	}


	@RequestMapping(value="/rest/user/{id}", method=RequestMethod.PUT)
	public User editUser(@PathVariable Integer id, @RequestParam("name") String name) {
		User user = User.fetchById(id);
		user.setName(name);
		user.update();
		return user;
	}

	@RequestMapping(value="/rest/user/{id}", method=RequestMethod.DELETE)
	public User deleteUser(@PathVariable Integer id) {
		User user = User.fetchById(id);
		user.delete();
		return user;
	}

	@RequestMapping(value="/rest/user/login", method=RequestMethod.POST)
	public User login(@RequestParam("name") String name) {
		// Fetch user by name, or create user if it doesn't exist
		User user = User.fetchByName(name);
		if (user != null) return user;
		user = new User(name);	
		user.save();
		return user;
	}


	// user fact history

	@RequestMapping(value="/rest/user_fact_history", method=RequestMethod.GET)
	public List<UserFactHistory> getUserFactHistory() {
		List<UserFactHistory> userFactHistoryist = UserFactHistory.fetchAll();
		return userFactHistoryist;
	}

	@RequestMapping(value="/rest/user_fact_history", method=RequestMethod.POST)
	public UserFactHistory addUserFactHistory(@RequestParam("user_id") int userId,
			@RequestParam("fact_id") int factId) {
		UserFactHistory userFactHistory = new UserFactHistory(userId, factId);
		userFactHistory.save();
		return userFactHistory;

	}

	@RequestMapping(value="/rest/user_fact_history/{id}", method=RequestMethod.GET)
	public UserFactHistory getUserFactHistory(@PathVariable Integer id) {
		return UserFactHistory.fetchById(id);
	}


	@RequestMapping(value="/rest/user_fact_history/{id}", method=RequestMethod.PUT)
	public UserFactHistory editUserFactHistory(@PathVariable Integer id,
			@RequestParam("user_id") int userId,
			@RequestParam("fact_id") int factId) {
		UserFactHistory userFactHistory = UserFactHistory.fetchById(id);
		userFactHistory.setUserId(userId);
		userFactHistory.setFactId(factId);
		userFactHistory.update();
		return userFactHistory;
	}

	@RequestMapping(value="/rest/user_fact_history/{id}", method=RequestMethod.DELETE)
	public UserFactHistory deleteUserFactHistory(@PathVariable Integer id) {
		UserFactHistory userFactHistory = UserFactHistory.fetchById(id);
		userFactHistory.delete();
		return userFactHistory;
	}

	// country route

	@RequestMapping(value="/rest/country_order", method=RequestMethod.GET)
	public List<CountryOrder> getCountryOrders() {
		List<CountryOrder> countryOrders = CountryOrder.fetchAll();
		return countryOrders;
	}

	@RequestMapping(value="/rest/country_order", method=RequestMethod.POST)
	public CountryOrder addCountryOrder(@RequestParam("country_id") int countryId,
			@RequestParam("route_order") int routeOrder, @RequestParam("poster_image") String posterImage, String name) {
		CountryOrder countryOrder = new CountryOrder(countryId, routeOrder, posterImage, name);
		countryOrder.save();
		return countryOrder;

	}

	@RequestMapping(value="/rest/country_order/{id}", method=RequestMethod.GET)
	public CountryOrder getCountryOrder(@PathVariable Integer id) {
		return CountryOrder.fetchById(id);
	}


	@RequestMapping(value="/rest/country_order/{id}", method=RequestMethod.PUT)
	public CountryOrder editCountryOrder(@PathVariable Integer id,
			@RequestParam("country_id") int countryId,
			@RequestParam("route_order") int routeOrder) {
		CountryOrder countryOrder = CountryOrder.fetchById(id);
		countryOrder.setCountryId(countryId);
		countryOrder.setRouteOrder(routeOrder);
		countryOrder.update();
		return countryOrder;
	}

	@RequestMapping(value="/rest/country_order/{id}", method=RequestMethod.DELETE)
	public CountryOrder deleteCountryOrder(@PathVariable Integer id) {
		CountryOrder countryOrder = CountryOrder.fetchById(id);
		countryOrder.delete();
		return countryOrder;
	}


	// fact_type_question_wording

	@RequestMapping(value="/rest/fact_type_question_wording", method=RequestMethod.GET)
	public List<FactTypeQuestionWording> getFactTypeQuestionWordings() {
		return FactTypeQuestionWording.fetchAll();
	}

	@RequestMapping(value="/rest/fact_type_question_wording", method=RequestMethod.POST)
	public FactTypeQuestionWording addFactTypeQuestionWording(
			@RequestParam("question_id") Integer questionId,
			@RequestParam("question_wording") String questionWording) {
		FactTypeQuestionWording factTypeQuestionWording = new FactTypeQuestionWording(questionId, questionWording);
		factTypeQuestionWording.save();
		return factTypeQuestionWording;
	}

	@RequestMapping(value="/rest/fact_type_question_wording/{id}", method=RequestMethod.GET)
	public FactTypeQuestionWording getFactTypeQuestionWording(@PathVariable Integer id) {
		return FactTypeQuestionWording.fetchById(id);
	}

	@RequestMapping(value="/rest/fact_type_question_wording/{id}", method=RequestMethod.PUT)
	public FactTypeQuestionWording editFactTypeQuestionWording(@PathVariable Integer id, @RequestParam("factId") Integer factId, @RequestParam("question_wording") String questionWording) {
		FactTypeQuestionWording factTypeQuestionWording = FactTypeQuestionWording.fetchById(id);
		factTypeQuestionWording.setFactId(factId);
		factTypeQuestionWording.setQuestionWording(questionWording);
		factTypeQuestionWording.update();
		return factTypeQuestionWording;
	}

	@RequestMapping(value="/rest/fact_type_question_wording/{id}", method=RequestMethod.DELETE)
	public FactTypeQuestionWording deleteFactTypeQuestionWording(@PathVariable Integer id) {
		FactTypeQuestionWording factTypeQuestionWording = FactTypeQuestionWording.fetchById(id);
		factTypeQuestionWording.delete();
		return factTypeQuestionWording;
	}


	// fact

	@RequestMapping(value="/rest/fact", method=RequestMethod.GET)
	public List<Fact> getFacts(@RequestParam("_start") int start, @RequestParam("_end") int end) {
		List<Fact> facts = Fact.fetchAll(start, end);
		return facts;
	}

	@RequestMapping(value="/rest/fact", method=RequestMethod.POST)
	public Fact addFact(@RequestParam("yago_id") String yagoId, @RequestParam("country_id") int countryId, @RequestParam("data") String data, @RequestParam("type_id") int factTypeId, 
			@RequestParam("label") String label, @RequestParam("rank") int rank) {
		Fact fact = new Fact(-1, "",countryId, data, factTypeId, label, rank, false);
		fact.save();
		return fact;
	}

	@RequestMapping(value="/rest/fact/{id}", method=RequestMethod.GET)
	public Fact getFact(@PathVariable Integer id) {
		return Fact.fetchById(id);
	}


	@RequestMapping(value="/rest/fact/{id}", method=RequestMethod.PUT)
	public Fact editFact(@PathVariable Integer id, @RequestParam("country_id") int countryId, @RequestParam("data") String data, @RequestParam("type_id") int factTypeId, @RequestParam("rank") int rank) {
		Fact fact = Fact.fetchById(id);
		fact.setCountryId(countryId);
		fact.setData(data);
		fact.setFactTypeId(factTypeId);
		fact.setRank(rank);
		fact.update();
		return fact;
	}

	@RequestMapping(value="/rest/fact/{id}", method=RequestMethod.DELETE)
	public Fact deleteFact(@PathVariable Integer id) {
		Fact fact = Fact.fetchById(id);
		fact.delete();
		return fact;
	}


}
