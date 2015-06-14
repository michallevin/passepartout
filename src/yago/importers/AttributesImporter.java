package yago.importers;

import java.io.FileNotFoundException;

import parsing.FactDictionary;
import db.models.Fact;

public class AttributesImporter extends BaseImporter {

	private static final String EN = "@en";
	private static final String FILENAME = "res//yagoInfoboxAttributes_en.tsv";

	public AttributesImporter() throws FileNotFoundException {
		super();
	}


	@Override
	public String getFileName() {
		return FILENAME;
	}

	private String replaceEntityMarker(String input) {
		if (input.startsWith("[[") && input.endsWith("]]"))
			return input.replace("[[", "<").replace("]]", ">").replace(" ", "_");
		return input;
	}

	@Override
	public void handleRow(String id, String attr1, String attr2, String attr3,
			String line) {

		if (!attr3.endsWith(EN) || attr3.length() > 255) return;
		Fact fact = Fact.parseFact(id, attr1, attr2, replaceEntityMarker(cleanInput(attr3, EN)));
		if (fact == null) return;
		Fact existing = FactDictionary.getInstance().getFactByYagoId(id);
		if (existing == null)  {
			FactDictionary.getInstance().addFact(fact);
		}
		else if (!existing.isUpdated()) {//make sure fact what not edited by user
			existing.updateFields(fact);
		}
	}



}






