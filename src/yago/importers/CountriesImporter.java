package yago.importers;


import java.io.FileNotFoundException;

import yago.CountryDictionary;
import db.models.Country;

public class CountriesImporter extends BaseImporter{

	private static final String FILENAME = "res//yagoTypes.tsv";
	private static final String WIKICAT_MEMBER_STATES_OF_THE_UNITED_NATIONS = "<wikicat_Member_states_of_the_United_Nations>";

	public CountriesImporter() throws FileNotFoundException {
		super();
	}

	@Override
	public String getFileName() {
		return FILENAME;
	}

	@Override
	public void handleRow(String id, String countryName, String attr2, String attr3,
			String line) {

		if (attr3.equals(WIKICAT_MEMBER_STATES_OF_THE_UNITED_NATIONS) && !countryName.contains("/")) {
			Country newCountry = Country.parseCountry(id, countryName);
			Country existing = CountryDictionary.getInstance().getCountry(countryName);

			if (existing == null) {
				newCountry.save();
				CountryDictionary.getInstance().getCountryMap().put(countryName, newCountry);
			}
			else if (!existing.isUpdated()) {
				existing.updateFields(newCountry);
			}
		}

	}

}






