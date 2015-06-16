package parsing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import db.models.Fact;

public class FactDictionary {

	private static FactDictionary instance = null;
	
	private HashMap<String, List<Fact>> factByDataMap;
	private HashMap<String, Fact> factsByYagoId;
	
	protected FactDictionary() {
		factByDataMap = new HashMap<String, List<Fact>>();
		factsByYagoId = new HashMap<String, Fact>();
		List<Fact> allFacts = Fact.fetchAll();
		for (Fact fact : allFacts) {
			String yagoId = fact.getYagoId();
			if (yagoId == null || yagoId.length() == 0) continue; // we don't want user input facts
			factsByYagoId.put(yagoId, fact);
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
				fact.setRank(fact.getRank() + 1);
		}
	}

	public HashMap<String, List<Fact>> getFactByDataMap() {
		return factByDataMap;
	}

	public void setLabel(String data, String label) {
		if (factByDataMap.containsKey(data)) {
			Collection<Fact> factList = factByDataMap.get(data);
			for (Fact fact : factList)
				fact.setLabel(label);
		}		
	}

	public Fact getFactByYagoId(String yagoId) {
		return factsByYagoId.containsKey(yagoId) ? factsByYagoId.get(yagoId) : null;
	}

	public void addFact(Fact fact) {
		if (!factByDataMap.containsKey(fact.getData()))
			factByDataMap.put(fact.getData(), new ArrayList<Fact>());
		factByDataMap.get(fact.getData()).add(fact);
		factsByYagoId.put(fact.getYagoId(), fact);
	}

	public int getCount() {
		return factsByYagoId.size();
	}

	public void clear() {
		factsByYagoId.clear();
		factByDataMap.clear();
		instance = null;
	}

	public static boolean hasInstance() {
		return instance != null;
	}


}
