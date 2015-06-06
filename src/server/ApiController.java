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
import db.models.Fact;
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
	
	
	// fact
	
	@RequestMapping(value="/rest/fact", method=RequestMethod.GET)
	public List<Fact> getFacts() {
		List<Fact> facts = Fact.fetchAll();
		return facts;
	}

	@RequestMapping(value="/rest/fact", method=RequestMethod.POST)
	public Fact addFact(@RequestParam("yagoId") String yagoId, @RequestParam("countryId") int countryId, @RequestParam("data") String data, @RequestParam("factTypeId") int factTypeId, @RequestParam("rank") int rank) {
		Fact fact = new Fact("",countryId,data,factTypeId,rank);
		fact.save();
		return fact;
	}
	
	@RequestMapping(value="/rest/fact/{id}", method=RequestMethod.GET)
	public Fact getFact(@PathVariable Integer id) {
		return Fact.fetchById(id);
	}
	

	@RequestMapping(value="/rest/fact/{id}", method=RequestMethod.PUT)
	public Fact editFact(@PathVariable Integer id, @RequestParam("countryId") int countryId, @RequestParam("data") String data, @RequestParam("factTypeId") int factTypeId, @RequestParam("rank") int rank) {
		Fact fact = Fact.fetchById(id);
		fact.setCountryId(countryId);
		fact.setData(data);
		fact.setFactTypeId(factTypeId);
		fact.setRank(rank);
		fact.update();
		return fact;
	}
	
	@RequestMapping(value="/rest/fact/{id}", method=RequestMethod.DELETE)
	public Country deleteFact(@PathVariable Integer id) {
		Country fact = Country.fetchById(id);
		fact.delete();
		return fact;
	}
	

}
