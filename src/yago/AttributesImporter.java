package yago;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import db.JDBCConnection;

public class AttributesImporter {

	private BufferedReader reader;


	public AttributesImporter() throws FileNotFoundException {
		reader = new BufferedReader(new FileReader("Resources//yagoSimpleTypes.tsv"));
	}

	public void importData() throws IOException {


		String str;
		while ((str = reader.readLine()) != null) {

			String[] attributes = str.split("\\t");

			if (attributes.length>=4) {

				YagoInfoboxAttribute infoboxAttribute = new YagoInfoboxAttribute(attributes[1],attributes[2],attributes[3]);
				infoboxAttribute.save();
			}



		}
	}
}






