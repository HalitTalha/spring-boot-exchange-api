package com.talha.openpayd.exchange.model;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.talha.openpayd.exchange.deserializer.CurrencyListResultDeserializer;

@JsonDeserialize(using = CurrencyListResultDeserializer.class)
public class CurrencyListResult {

	private List<String> currencyCodes;

	public List<String> getCurrencyCodes() {
		return currencyCodes;
	}

	public void setCurrencyCodes(List<String> currencyCodes) {
		this.currencyCodes = currencyCodes;
	}

}
