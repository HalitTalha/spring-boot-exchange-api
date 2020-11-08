package com.talha.openpayd.exchange.deserializer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.talha.openpayd.exchange.model.CurrencyPair;
import com.talha.openpayd.exchange.model.Rate;

class RateDeserializerTest {

	@Test
	void readRateTest() throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		String rate = "{\"query\":{\"count\":1},\"results\":{\"USD_PHP\":{\"id\":\"USD_PHP\",\"val\":48.435014,\"to\":\"PHP\",\"fr\":\"USD\"}}}";
		Rate rateObject = objectMapper.readValue(rate, Rate.class);
		assertEquals(createRate(), rateObject);
	}

	private Rate createRate() {
		Rate rate = new Rate();
		rate.setCurrencyPair(new CurrencyPair("USD", "PHP"));
		rate.setValue(48.435014);
		return rate;
	}
}
