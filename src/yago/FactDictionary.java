package yago;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import db.models.Fact;

public class FactDictionary {

	private static FactDictionary instance = null;
	
	private HashMap<String, List<Fact>> factMap;
	
	protected FactDictionary() {
		factMap = new HashMap<String, List<Fact>>();
		List<Fact> allFacts = Fact.fetchAll();
		for (Fact fact : allFacts) {
			if (!factMap.containsKey(fact.getData()))
				factMap.put(fact.getData(), new ArrayList<Fact>());
			factMap.get(fact.getData()).add(fact);
		}
	} 
	
	public static FactDictionary getInstance() {
		if (instance == null) {
			instance = new FactDictionary();
		}
		return instance;
	}
	
	public void addLink(String data) {
		if (factMap.containsKey(data)) {
			List<Fact> factList = factMap.get(data);
			for (Fact fact : factList)
				fact.setRank(fact.getRank() + 1);
		}
	}

	public HashMap<String, List<Fact>> getFactMap() {
		return factMap;
	}

	public void setLabel(String data, String label) {
		if (factMap.containsKey(data)) {
			List<Fact> factList = factMap.get(data);
			for (Fact fact : factList)
				fact.setLabel(label);
		}		
	}

}
