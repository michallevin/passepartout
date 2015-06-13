package yago;

import db.models.Country;
import db.models.CountryOrder;
import db.models.FactType;
import db.models.FactTypeQuestionWording;

public class GameConfigInit {

	public static void setCountryOrder() {
		String[][] countries = {
				{"<Belgium>", "belgium.png"}, 
				{"<Repbulic_Of_Ireland>", "ireland.png"},
				{"<Germany>", "germany.png"}, 
				{"<United_Kingdom>", "uk.png"}
		};
		
		
		for (int i = 0; i < countries.length; ++i) {
			Country country = CountryDictionary.getInstance().getCountry(countries[i][0]);
			CountryOrder countryOrder = new CountryOrder(country.getId(), i, countries[i][1]);
			countryOrder.replace();
		}
	}
	

	public static void setQuestionWordings() {
		String[][] questionWordings = {
				{"<citizefOf>", "Who was born in $countryName?"}, 
		};
		
		for (int i = 0; i < questionWordings.length; ++i) {
			FactType factType = TypeDictionary.getInstance().getType(questionWordings[i][0]);
			FactTypeQuestionWording factTypeQuestionWording = new FactTypeQuestionWording(factType.getId(), questionWordings[i][1]);
			factTypeQuestionWording.replace();
		}
	}
}
