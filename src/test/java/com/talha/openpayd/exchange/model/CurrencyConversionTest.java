package com.talha.openpayd.exchange.model;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class CurrencyConversionTest {

	@Test
	void equalsTest_Verify() {
		EqualsVerifier.simple().forClass(CurrencyConversion.class).verify();
	}

}
