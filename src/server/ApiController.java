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
import db.models.Highscore;


@RestController
public class ApiController {
	
	@RequestMapping(value="/rest/question", method=RequestMethod.GET)
	public List<Question> getQuestions() {
		
		List<Country> countries = Country.fetchByOrder();
		List<Question> questions = new ArrayList<Question>();
		for (Country country : countries)
			questions.add(Question.generateQuestion(country));

		return questions;
	}

	
	/* import */ 
	
	@RequestMapping(value="/import/start", method=RequestMethod.GET)
	public void startImport() {
		Thread t = new Thread() {
			 public void run() {
				 if (!YagoImport.isImporting())
					 YagoImport.startImport(true, true, true, true, true);
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
		return Highscore.fetchAll();
	}
	
	@RequestMapping(value="/rest/highscore}", method=RequestMethod.POST)
	public Highscore addHighScore(@RequestParam("name") String name, @RequestParam("score") Integer score) {
		Highscore highscore = new Highscore(name, score);
		highscore.save();
		return highscore;
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
}
