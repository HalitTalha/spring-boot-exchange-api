package com.talha.openpayd.exchange.deserializer;

import static com.talha.openpayd.exchange.deserializer.Constants.ID_NODE;
import static com.talha.openpayd.exchange.deserializer.Constants.RESULT_NODE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.talha.openpayd.exchange.model.CurrencyListResult;;

public class CurrencyListResultDeserializer extends StdDeserializer<CurrencyListResult> {

	private static final long serialVersionUID = 7554643379165664012L;

	public CurrencyListResultDeserializer() {
		this(null);
	}

	protected CurrencyListResultDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public CurrencyListResult deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		CurrencyListResult currencyListResult = new CurrencyListResult();
		currencyListResult.setCurrencyCodes(extractCurrencyCodes(jp));
		return currencyListResult;
	}

	private List<String> extractCurrencyCodes(JsonParser jp) throws IOException {
		List<String> currencyCodes = new ArrayList<String>();
		Iterator<JsonNode> resultElements = getResultElements(jp);
		while (resultElements.hasNext()) {
			JsonNode currencyNode = resultElements.next();
			currencyCodes.add(currencyNode.get(ID_NODE).asText());
		}
		return currencyCodes;
	}

	private Iterator<JsonNode> getResultElements(JsonParser jp) throws IOException {
		JsonNode topNode = jp.getCodec().readTree(jp);
		JsonNode resultsNode = topNode.get(RESULT_NODE);
		return resultsNode.elements();
	}

}
