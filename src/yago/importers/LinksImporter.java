package yago.importers;

import java.io.FileNotFoundException;

import db.models.Link;

public class LinksImporter extends BaseImporter {

	private static final String FILENAME = "res//yagoWikipediaInfo.tsv";
	private static final String LINKS_TO = "<linksTo>";

	public LinksImporter() throws FileNotFoundException {
		super();
	}

	@Override
	public String getFileName() {
		return FILENAME;
	}

	@Override
	public void handleRow(String id, String attr1, String attr2, String attr3,
			String line) {
		if (attr2.equals(LINKS_TO)) {
			Link link = new Link(attr1, attr3);
			link.save();
		}
		
	}
}






