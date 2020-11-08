package com.talha.openpayd.exchange.model;

import java.util.Objects;

import com.talha.openpayd.exchange.annotation.ValidCurrencyCode;

public class CurrencyPair {

	@ValidCurrencyCode
	private String source;

	@ValidCurrencyCode
	private String target;

	public CurrencyPair() {
		super();
	}

	public CurrencyPair(String source, String target) {
		super();
		this.source = source;
		this.target = target;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@Override
	public String toString() {
		return source + "_" + target;
	}

	@Override
	public int hashCode() {
		return Objects.hash(source, target);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CurrencyPair other = (CurrencyPair) obj;
		return Objects.equals(source, other.source) && Objects.equals(target, other.target);
	}

}
