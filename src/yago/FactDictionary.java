package yago;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import db.models.Fact;

public class FactDictionary {

	private static FactDictionary instance = null;
	
	private HashMap<String, HashMap<String, Fact>> factMap;
	
	protected FactDictionary() {
		factMap = new HashMap<String, HashMap<String, Fact>>();
		List<Fact> allFacts = Fact.fetchAll();
		for (Fact fact : allFacts) {
			if (fact.getYagoId() == null || fact.getYagoId().length() == 0) continue;
			if (!factMap.containsKey(fact.getData()))
				factMap.put(fact.getData(), new  HashMap<String, Fact>());
			factMap.get(fact.getData()).put(fact.getYagoId(), fact);
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
			Collection<Fact> factList = factMap.get(data).values();
			for (Fact fact : factList)
				fact.setRank(fact.getRank() + 1);
		}
	}

	public HashMap<String, HashMap<String, Fact>> getFactMap() {
		return factMap;
	}

	public void setLabel(String data, String label) {
		if (factMap.containsKey(data)) {
			Collection<Fact> factList = factMap.get(data).values();
			for (Fact fact : factList)
				fact.setLabel(label);
		}		
	}

	public Fact getFact(Fact fact) {

		if (factMap.containsKey(fact.getData())) {
			HashMap<String, Fact> factsWithData = factMap.get(fact.getData());
			if (factsWithData.containsKey(fact.getYagoId()))
				return factsWithData.get(fact.getYagoId());
		}			
		return null;
	}

	public void addFact(Fact fact) {
		if (!factMap.containsKey(fact.getData()))
			factMap.put(fact.getData(), new HashMap<String, Fact>());
		factMap.get(fact.getData()).put(fact.getYagoId(), fact);
	}


}
