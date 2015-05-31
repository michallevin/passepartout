package yago;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import db.models.FactType;

public class TypeDictionary {

	private static TypeDictionary instance = null;
	static private Random r = new Random();

	private HashMap<String, Integer> typeMap;

	protected TypeDictionary() {
		
		typeMap = new HashMap<String, Integer>();
		List<FactType> allTypes = FactType.fetchAll();
		for (FactType fact : allTypes) {
			typeMap.put(fact.getTypeName(), fact.getId());
		}

	} 

	public static TypeDictionary getInstance() {
		if (instance == null) {
			instance = new TypeDictionary();
		}
		return instance;
	}

	public Integer getId(String typeName) {

		if (typeMap.containsKey(typeName)) {
			return typeMap.get(typeName);
		}
		else {
			FactType fact = new FactType(-1, typeName);
			int id = fact.save();
			if (id != -1) {
				typeMap.put(typeName, id);
			}
			return id;
		}

	}
	
	public FactType getRandomFactType() {
		
		int index = r.nextInt(typeMap.size());
		return (FactType) typeMap.values().toArray()[index];
	}
	

}

