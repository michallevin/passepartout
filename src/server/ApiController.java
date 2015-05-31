package server;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

	@RequestMapping(value="/import/start", method=RequestMethod.GET)
	public void startImport() {
		Thread t = new Thread() {
			 public void run() {
				 if (!YagoImport.isImporting())
					 YagoImport.startImport(false, false, false, true, true);
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
	public Country addCountry() {
		return null;
	}
	
	@RequestMapping(value="/rest/country/{id}", method=RequestMethod.GET)
	public Country getCountry(@PathVariable Integer id) {
		return null;
	}
	

	@RequestMapping(value="/rest/country/{id}", method=RequestMethod.PUT)
	public Country editCountry(@PathVariable Integer id) {
		return null;
	}
	
	@RequestMapping(value="/rest/country/{id}", method=RequestMethod.DELETE)
	public Country deleteCountry(@PathVariable Integer id) {
		return null;
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
}
