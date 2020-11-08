package com.talha.openpayd.exchange.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.talha.openpayd.exchange.model.CurrencyConversion;
import com.talha.openpayd.exchange.model.CurrencyPair;
import com.talha.openpayd.exchange.model.Rate;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class CurrencyConversionRepositoryIT {

	@Autowired
	private CurrencyConversionRepository repository;

	@Test
	void save_thenSuccessfull() {
		CurrencyConversion conversion = repository.save(createCurrencyConversion());
		assertTrue(conversion.getId() != 0L);
	}

	@Test
	void list_ByTransactionId_thenSuccessfull() {
		CurrencyConversion conversion = repository.save(createCurrencyConversion());
		CurrencyConversion result = repository.findByTransactionId(conversion.getTransactionId()).get();
		assertTrue(conversion.getTransactionId().equals(result.getTransactionId()));
	}

	private Rate createRate() {
		Rate rate = new Rate();
		rate.setCurrencyPair(createCurrencyPair());
		rate.setValue(1d);
		return rate;
	}

	private CurrencyPair createCurrencyPair() {
		return new CurrencyPair("TRY", "USD");
	}

	private CurrencyConversion createCurrencyConversion() {
		CurrencyConversion conversion = new CurrencyConversion();
		conversion.setRate(createRate());
		conversion.setSourceAmount(1.2d);
		conversion.setTargetAmount(1.2d);
		return conversion;
	}

}
