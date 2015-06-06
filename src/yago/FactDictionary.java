package yago;

import java.util.HashMap;
import java.util.List;

import db.models.Fact;

public class FactDictionary {

	private static FactDictionary instance = null;
	
	private HashMap<String, Integer> factMap;
	
	protected FactDictionary() {
		factMap = new HashMap<String, Integer>();
		List<Fact> allFacts = Fact.fetchAll();
		for (Fact fact : allFacts) {
			factMap.put(fact.getData(), 0);
		}
	} 
	
	public static FactDictionary getInstance() {
		if (instance == null) {
			instance = new FactDictionary();
		}
		return instance;
	}
	
	public void addFact(String data) {
		if (!factMap.containsKey(data)) {
			factMap.put(data, 0);
		}
	}
	
	public void addLink(String data) {
		if (factMap.containsKey(data)) {
			factMap.put(data, 1 + instance.factMap.get(data));
		}
	}

	public HashMap<String, Integer> getFactMap() {
		return factMap;
	}

}
