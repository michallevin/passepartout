package parsing;

import java.util.HashMap;
import java.util.List;

import db.models.FactType;

public class TypeDictionary {

	private static TypeDictionary instance = null;

	private HashMap<String, FactType> typeMap;

	protected TypeDictionary() {
		
		typeMap = new HashMap<String, FactType>();
		List<FactType> allTypes = FactType.fetchAll();
		for (FactType type : allTypes) {
			typeMap.put(type.getTypeName(), type);
		}

	} 

	public static TypeDictionary getInstance() {
		if (instance == null) {
			instance = new TypeDictionary();
		}
		return instance;
	}

	public Integer getId(String typeName, boolean isLiteral) {

		if (typeMap.containsKey(typeName)) {
			return typeMap.get(typeName).getId();
		}
		else {
			FactType factType = new FactType(-1, typeName, isLiteral);
			int id = factType.save();
			if (id != -1) {
				typeMap.put(typeName, factType);
			}
			return id;
		}

	}

	public FactType getType(String typeName) {
		if (typeMap.containsKey(typeName)) {
			return typeMap.get(typeName);
		}
		return null;
	}
	

}

