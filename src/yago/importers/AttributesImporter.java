package yago.importers;

import java.io.FileNotFoundException;

import db.models.Fact;

public class AttributesImporter extends BaseImporter {

	private static final String FILENAME = "res//yagoInfoboxAttributes_en.tsv";

	public AttributesImporter() throws FileNotFoundException {
		super();
	}
	
	

	@Override
	public String getFileName() {
		return FILENAME;
	}

	@Override
	public void handleRow(String id, String attr1, String attr2, String attr3,
			String line) {
		Fact fact = new Fact(attr1, attr2, attr3);
		fact.save();
		
	}
}






