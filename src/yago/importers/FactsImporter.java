package yago.importers;

import java.io.FileNotFoundException;

import yago.FactDictionary;
import db.models.Fact;

public class FactsImporter extends BaseImporter {

	private static final String FILENAME = "res//yagoFacts.tsv";

	public FactsImporter() throws FileNotFoundException {
		super();
	}

	@Override
	public String getFileName() {
		return FILENAME;
	}

	@Override
	public void handleRow(String id, String attr1, String attr2, String attr3,
			String line) {
		
		if (attr3.length() > 255) return;
		Fact fact = Fact.parseFact(id, attr1, attr2,  attr3);
		if (fact != null) {
			Fact existing = FactDictionary.getInstance().getFact(fact);
			if (existing == null)  {
				//fact.save();
				FactDictionary.getInstance().addFact(fact);
			}
			else {
				existing.updateFields(fact);
			}
		}
		
	}
}






