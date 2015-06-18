package parsing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import db.models.Fact;

public class FactDictionary {

	private static FactDictionary instance = null;
	
	private HashMap<String, List<Fact>> factByDataMap;
	private HashMap<String, Fact> factsByYagoIdMap;
	
	protected FactDictionary() {
		factByDataMap = new HashMap<String, List<Fact>>();
		factsByYagoIdMap = new HashMap<String, Fact>();
		List<Fact> allFacts = Fact.fetchAll();
		for (Fact fact : allFacts) {
			String yagoId = fact.getYagoId();
			if (yagoId == null || yagoId.length() == 0) continue; // we don't want user input facts
			factsByYagoIdMap.put(yagoId, fact);
			if (!factByDataMap.containsKey(fact.getData()))
				factByDataMap.put(fact.getData(), new ArrayList<Fact>());
			factByDataMap.get(fact.getData()).add(fact);
		}
	} 
	
	public static FactDictionary getInstance() {
		if (instance == null) {
			instance = new FactDictionary();
		}
		return instance;
	}
		
	public void addLink(String data) {
		if (factByDataMap.containsKey(data)) {
			Collection<Fact> factList = factByDataMap.get(data);
			for (Fact fact : factList)
				fact.incRank();
		}
	}



	public void setLabel(String data, String label) {
		if (factByDataMap.containsKey(data)) {
			Collection<Fact> factList = factByDataMap.get(data);
			for (Fact fact : factList)
				fact.setTempLabel(label);
		}		
	}

	public Fact getFactByYagoId(String yagoId) {
		return factsByYagoIdMap.containsKey(yagoId) ? factsByYagoIdMap.get(yagoId) : null;
	}

	public void addFact(Fact fact) {
		if (!factByDataMap.containsKey(fact.getData()))
			factByDataMap.put(fact.getData(), new ArrayList<Fact>());
		factByDataMap.get(fact.getData()).add(fact);
		factsByYagoIdMap.put(fact.getYagoId(), fact);
	}

	public void clear() {
		factsByYagoIdMap.clear();
		factByDataMap.clear();
		instance = null;
	}

	public static boolean hasInstance() {
		return instance != null;
	}

	public HashMap<String, Fact> getFactsByYagoIdMap() {
		return factsByYagoIdMap;
	}


}
