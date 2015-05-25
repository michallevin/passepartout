package yago;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import db.JDBCConnection;

public class CountriesImporter {

	private BufferedReader reader;


	public CountriesImporter() throws FileNotFoundException {
		reader = new BufferedReader(new FileReader("Resources//yagoInfoboxTemplates_en.tsv"));
	}

	public void importData() throws IOException {


		String str;
		while ((str = reader.readLine()) != null) {

			String[] attributes = str.split("\\t");

			if (attributes.length>=4) {

				if (attributes[3].equals("\"country\"@en")) {
					
				}
				YagoInfoboxAttribute infoboxAttribute = new YagoInfoboxAttribute(attributes[1],attributes[2],attributes[3]);
				infoboxAttribute.save();
			}



		}
	}
}






