package com.talha.openpayd.exchange.model;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class RateTest {

	@Test
	void equalsTest_Verify() {
		EqualsVerifier.simple().forClass(Rate.class);
	}

}
