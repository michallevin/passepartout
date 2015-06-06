package server;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import core.Question;
import yago.YagoImport;
import db.models.Country;
import db.models.CountryOrder;
import db.models.FactType;
import db.models.Highscore;
import db.models.User;
import db.models.UserFactHistory;


@RestController
public class ApiController {
	
	@RequestMapping(value="/rest/question", method=RequestMethod.GET)
	public List<Question> getQuestions(@RequestParam("userId") Integer userId) {
			
		List<Country> countries = Country.fetchByOrder();
		List<Question> questions = new ArrayList<Question>();
		int i = 0;
		for (Country country : countries) {
			questions.add(Question.generateQuestion(country, userId, i % 2 == 0));
			i += 1;
		}

		return questions;
	}

	
	/* import */ 
	
	@RequestMapping(value="/import/start", method=RequestMethod.GET)
	public void startImport() {
		Thread t = new Thread() {
			 public void run() {
				 if (!YagoImport.isImporting())
					 YagoImport.startImport(true, false, false, true, false);
				 else {
					 
				 }
			 }
		};
		t.start();
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
	public Country addCountry(@RequestParam("name") String name) {
		Country country = new Country(-1, "", name);
		country.save();
		return country;
		
	}
	
	@RequestMapping(value="/rest/country/{id}", method=RequestMethod.GET)
	public Country getCountry(@PathVariable Integer id) {
		return Country.fetchById(id);
	}
	

	@RequestMapping(value="/rest/country/{id}", method=RequestMethod.PUT)
	public Country editCountry(@PathVariable Integer id, @RequestParam("name") String name) {
		Country country = Country.fetchById(id);
		country.setName(name);
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
		return null;
	}
	
	@RequestMapping(value="/rest/highscore", method=RequestMethod.POST)
	public Highscore addHighScore() {
		return null;
	}
	
	@RequestMapping(value="/rest/highscore/{id}", method=RequestMethod.GET)
	public Highscore getHighscore(@PathVariable Integer id) {
		return null;
	}
	
	@RequestMapping(value="/rest/highscore/{id}", method=RequestMethod.PUT)
	public Highscore editHighscore(@PathVariable Integer id) {
		return null;
	}
	
	@RequestMapping(value="/rest/highscore/{id}", method=RequestMethod.DELETE)
	public Highscore deleteHighscore(@PathVariable Integer id) {
		return null;
	}
	
	//fact type
	
	@RequestMapping(value="/rest/fact_type", method=RequestMethod.GET)
	public List<FactType> getFactTypes() {
		List<FactType> countries = FactType.fetchAll();
		return countries;
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
	                                    @RequestParam("route_order") int routeOrder) {
		CountryOrder countryOrder = new CountryOrder(countryId, routeOrder);
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
	
}
