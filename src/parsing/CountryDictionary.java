package parsing;

import java.util.HashMap;
import java.util.List;

import db.models.Country;

public class CountryDictionary {

	private static CountryDictionary instance = null;
	
	private HashMap<String, Country> countryMap;
	
	protected CountryDictionary() {
		setCountryMap(new HashMap<String, Country>());
		List<Country> allCountries = Country.fetchAll();
		for (Country country : allCountries) {
			getCountryMap().put(country.getName(), country);
		}
	} 
	
	public static CountryDictionary getInstance() {
		if (instance == null) {
			instance = new CountryDictionary();
		}
		return instance;
	}
	
	public Country getCountry(String countryName) {
		if (getCountryMap().containsKey(countryName)) {
			return getCountryMap().get(countryName);
		}
		else {
			return null;
		}
	}
	
	public void setLabel(String name, String label) {
		if (getCountryMap().containsKey(name)) {
			Country country = getCountryMap().get(name);
			country.setLabel(label);
		}		
	}

	public HashMap<String, Country> getCountryMap() {
		return countryMap;
	}

	public void setCountryMap(HashMap<String, Country> countryMap) {
		this.countryMap = countryMap;
	}
	
	
}
