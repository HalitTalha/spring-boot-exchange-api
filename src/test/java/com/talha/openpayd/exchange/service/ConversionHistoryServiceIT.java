package com.talha.openpayd.exchange.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.talha.openpayd.exchange.model.CurrencyConversion;
import com.talha.openpayd.exchange.model.CurrencyPair;
import com.talha.openpayd.exchange.model.Rate;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD) // to recreate mock db before each test
class ConversionHistoryServiceIT {

	@Autowired
	private ConversionHistoryService service;

	@MockBean
	private DateTimeProvider dateTimeProvider;

	@SpyBean
	private AuditingHandler handler;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		handler.setDateTimeProvider(dateTimeProvider);
	}

	@Test
	void getByConversionDate_Valid() throws ParseException {
		when(dateTimeProvider.getNow()).thenReturn(Optional.of(createLocalDateTime("2020-10-17 00:00")));
		CurrencyConversion conversion = service.save(createCurrencyConversion());
		service.save(createCurrencyConversion());
		service.save(createCurrencyConversion());
		when(dateTimeProvider.getNow()).thenReturn(Optional.of(createLocalDateTime("2020-10-16 00:00")));
		service.save(createCurrencyConversion());
		service.save(createCurrencyConversion());
		List<CurrencyConversion> list = service.listByConversionDate(conversion.getConversionDate().toLocalDate(),
				PageRequest.of(0, 10));
		assertAll(() -> assertTrue(list.size() == 3), () -> {
			assertTrue(list.stream().allMatch(item -> conversion.getConversionDate().equals(item.getConversionDate())));
		});
	}

	@Test
	void getByConversionDateAndTransactionId_Valid() throws ParseException {
		when(dateTimeProvider.getNow()).thenReturn(Optional.of(createLocalDateTime("2020-10-17 00:00")));
		CurrencyConversion transactionIdToFindConversion = createCurrencyConversion();
		CurrencyConversion conversion = service.save(transactionIdToFindConversion);
		service.save(createCurrencyConversion());
		service.save(createCurrencyConversion());
		when(dateTimeProvider.getNow()).thenReturn(Optional.of(createLocalDateTime("2020-10-16 00:00")));
		service.save(createCurrencyConversion());
		service.save(createCurrencyConversion());
		CurrencyConversion result = service.getByConversionDateAndTransactionId(
				conversion.getConversionDate().toLocalDate(), conversion.getTransactionId());
		assertEquals(conversion, result);
	}

	@Test
	void getByTransactionIdTest_Valid() throws ParseException {
		when(dateTimeProvider.getNow()).thenReturn(Optional.of(createLocalDateTime("2020-10-17 00:00")));
		CurrencyConversion transactionIdToFindConversion = createCurrencyConversion();
		CurrencyConversion conversion = service.save(transactionIdToFindConversion);
		service.save(createCurrencyConversion());
		service.save(createCurrencyConversion());
		when(dateTimeProvider.getNow()).thenReturn(Optional.of(createLocalDateTime("2020-10-16 00:00")));
		service.save(createCurrencyConversion());
		service.save(createCurrencyConversion());
		CurrencyConversion result = service.getByTransactionId(conversion.getTransactionId());
		assertEquals(conversion, result);
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

	private CurrencyConversion createCurrencyConversion() throws ParseException {
		CurrencyConversion conversion = new CurrencyConversion();
		conversion.setRate(createRate());
		conversion.setSourceAmount(1.2d);
		conversion.setTargetAmount(1.2d);
		return conversion;
	}

	private LocalDateTime createLocalDateTime(String date) {
		return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
	}

}
