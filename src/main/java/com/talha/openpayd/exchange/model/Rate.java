package com.talha.openpayd.exchange.model;

import java.util.Objects;

import javax.persistence.Embedded;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.talha.openpayd.exchange.deserializer.RateDeserializer;

@JsonDeserialize(using = RateDeserializer.class)
public class Rate {

	@Embedded
	@NotNull
	private CurrencyPair currencyPair;

	@NotNull
	private Double value;

	public Rate() {
		super();
	}

	public CurrencyPair getCurrencyPair() {
		return currencyPair;
	}

	public void setCurrencyPair(CurrencyPair currencyPair) {
		this.currencyPair = currencyPair;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(currencyPair, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rate other = (Rate) obj;
		return Objects.equals(currencyPair, other.currencyPair) && Objects.equals(value, other.value);
	}

}
