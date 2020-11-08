package com.talha.openpayd.exchange.deserializer;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.talha.openpayd.exchange.model.CurrencyListResult;

class CurrencyListResultDeserializerTest {

	@Test
	void readCurrenciesTest() throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		String currencies = "{\"results\":{\"ALL\":{\"currencyName\":\"Albanian Lek\",\"currencySymbol\":\"Lek\",\"id\":\"ALL\"},\"XCD\":{\"currencyName\":\"East Caribbean Dollar\",\"currencySymbol\":\"$\",\"id\":\"XCD\"}}}";
		CurrencyListResult currencyListResultObject = objectMapper.readValue(currencies, CurrencyListResult.class);
		assertTrue(
				createCurrencyListResult().getCurrencyCodes().containsAll(currencyListResultObject.getCurrencyCodes()));
	}

	private CurrencyListResult createCurrencyListResult() {
		CurrencyListResult result = new CurrencyListResult();
		List<String> list = new ArrayList<String>();
		list.add("ALL");
		list.add("XCD");
		result.setCurrencyCodes(list);
		return result;
	}
}
