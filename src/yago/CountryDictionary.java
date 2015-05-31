package yago;

import java.util.HashMap;
import java.util.List;

import db.models.Country;

public class CountryDictionary {

	private static CountryDictionary instance = null;
	
	private HashMap<String, Integer> countryMap;
	
	protected CountryDictionary() {
		countryMap = new HashMap<String, Integer>();
		List<Country> allCountries = Country.fetchAll();
		for (Country country : allCountries) {
			countryMap.put(country.getName(), country.getId());
		}
	} 
	
	public static CountryDictionary getInstance() {
		if (instance == null) {
			instance = new CountryDictionary();
		}
		return instance;
	}
	
	public Integer getCountryId(String countryName) {
		if (instance.countryMap.containsKey(countryName)) {
			return instance.countryMap.get(countryName);
		}
		else {
			return null;
		}
	}
}
