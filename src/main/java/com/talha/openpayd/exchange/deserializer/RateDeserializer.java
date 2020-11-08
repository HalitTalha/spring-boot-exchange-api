package com.talha.openpayd.exchange.deserializer;

import static com.talha.openpayd.exchange.deserializer.Constants.FROM_NODE;
import static com.talha.openpayd.exchange.deserializer.Constants.RESULT_NODE;
import static com.talha.openpayd.exchange.deserializer.Constants.TO_NODE;
import static com.talha.openpayd.exchange.deserializer.Constants.VALUE_NODE;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.talha.openpayd.exchange.model.CurrencyPair;
import com.talha.openpayd.exchange.model.Rate;

public class RateDeserializer extends StdDeserializer<Rate> {

	private static final long serialVersionUID = 710588790136784L;

	public RateDeserializer() {
		this(null);
	}

	protected RateDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public Rate deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		JsonNode rateNode = getRateNode(jp);
		return extractRate(rateNode);
	}

	private Rate extractRate(JsonNode rateNode) {
		Rate rate = new Rate();
		rate.setCurrencyPair(extractCurrencyPair(rateNode));
		rate.setValue(rateNode.get(VALUE_NODE).asDouble());
		return rate;
	}

	private CurrencyPair extractCurrencyPair(JsonNode rateNode) {
		return new CurrencyPair(rateNode.get(FROM_NODE).asText(), rateNode.get(TO_NODE).asText());
	}

	private JsonNode getRateNode(JsonParser jp) throws IOException {
		JsonNode topNode = jp.getCodec().readTree(jp);
		return topNode.get(RESULT_NODE).elements().next();
	}
}
