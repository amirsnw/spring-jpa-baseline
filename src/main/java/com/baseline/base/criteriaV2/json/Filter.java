package com.baseline.base.criteriaV2.json;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class Filter {

	private String property;
	private String value;
	private Operator operator;
	
	public Filter() {
	}

	@SuppressWarnings("unchecked")
	public Filter(String json) throws JsonParseException, JsonMappingException, IOException {
		Map<String, String> filter = new ObjectMapper().readValue(json, Map.class);

		this.property = filter.get("property");
		this.value = filter.get("value");
		this.operator = Operator.getValue(filter.get("operator"));
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}
}
