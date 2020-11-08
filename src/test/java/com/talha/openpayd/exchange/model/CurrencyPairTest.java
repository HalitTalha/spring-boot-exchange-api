package com.talha.openpayd.exchange.model;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class CurrencyPairTest {

	@Test
	void equalsTest_Verify() {
		EqualsVerifier.simple().forClass(CurrencyPair.class).verify();
	}
}
