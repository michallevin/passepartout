package core;

import yago.CountryDictionary;
import yago.TypeDictionary;
import db.models.Country;
import db.models.CountryOrder;
import db.models.FactType;
import db.models.FactTypeQuestionWording;

public class GameConfigInit {

	public static void setCountryOrder() {
		String[][] countries = {
				{"<Belgium>", "belgium2.jpg"}, 
				{"<France>", "paris1.jpg"},
				{"<Germany>", "germany1.jpg"},
				{"<Liechtenstein>", "lichtenstien1.jpg"},
				{"<Italy>", "italy1.jpg"},
				{"<Greece>", "greece1.jpg"},
				{"<Turkey>", "turkey4.jpg"},
				{"<Egypt>", "egypt3.jpg"},
				{"<Yemen>", "yemen1.jpg"},
				{"<India>", "india1.jpg"},
				{"<India>", "india3.jpg"},
				{"<Thailand>", "thailand3.jpg"},
				{"<China>", "china2.jpg"},
				{"<Japan>", "japan1.jpg"},
				{"<United_States>", "san_fransisco1.jpg"},
				{"<United_States>", "new_york4.jpg"},
				{"<Republic_of_Ireland>", "ireland2.jpg"},
				{"<United_Kingdom>", "london3.jpg"}
		};
		
		
		for (int i = 0; i < countries.length; ++i) {
			Country country = CountryDictionary.getInstance().getCountry(countries[i][0]);
			CountryOrder countryOrder = new CountryOrder(country.getId(), i, countries[i][1]);
			countryOrder.save();
		}
	}
	
	public static void setQuestionWordings() {
		String[][] questionWordings = {
				{"<happenedIn>", "Which of these happend in $countryName?"}, 
				{"<isCitizenOf>", "Who is/was a citizen of $countryName?"}, 
				{"<isPoliticianOf>", "Who is/was a politician of $countryName?"}, 
				{"<infobox/en/basincountries>", "Which of these is located in $countryName?"}, 
				{"<infobox/en/locationcountry>", "Which company is located in $countryName?"}, 
				{"<infobox/en/nationality>", "Who was born in $countryName?"}, 
				{"<infobox/en/origin>", "Which of the following has its origin in $countryName?"}, 
				{"<participatedIn>", "In which event did $countryName participate in?"}, 
				{"<livesIn>", "Which of these people lives/lived in $countryName?"}, 
				{"<infobox/en/establisheddate>", "When was $countryName established?"}, 
				{"<infobox/en/stateparty>", "Which of the following is the a state in $countryName?"}, 
				{"<infobox/en/leadername>", "Who is the leader of $countryName?"}, 
				{"<isLeaderOf>", "Who is the leader of $countryName?"}, 
				{"<hasOfficialLanguage>", "Which of these is an official language of $countryName?"}, 
				{"<hasNumberOfPeople>", "How many people live in $countryName?"}, 
				{"<infobox/en/headquarters>", "Which of these has its headquarters in $countryName?"}, 
				{"<infobox/en/imagemap>", "Which of these is $countryName?"}, 
				{"<hasMotto>", "Which of these is the motto of $countryName?"}, 
				{"<infobox/en/imageflag>", "Which of these is the flag of $countryName?"}, 
				{"<infobox/en/areakm>", "What is the area of $countryName?"}, 
				{"<infobox/en/cctld>", "What is the TLD of $countryName?"}, 
				{"<infobox/en/capital>", "What is the capital of $countryName?"}, 
				{"<infobox/en/currencycode>", "What is the currency code of $countryName?"}, 
				{"<infobox/en/currencycode>", "What is the currency code of $countryName?"}, 
				{"<hasCapital>", "What is the capital of $countryName?"}, 
	
		};
		
		for (int i = 0; i < questionWordings.length; ++i) {
			FactType factType = TypeDictionary.getInstance().getType(questionWordings[i][0]);
			FactTypeQuestionWording factTypeQuestionWording = new FactTypeQuestionWording(factType.getId(), questionWordings[i][1]);
			factTypeQuestionWording.save();
		}
	}
}
