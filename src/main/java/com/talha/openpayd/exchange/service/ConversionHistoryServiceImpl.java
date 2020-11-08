package com.talha.openpayd.exchange.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.talha.openpayd.exchange.model.CurrencyConversion;
import com.talha.openpayd.exchange.repository.CurrencyConversionRepository;

@Service
public class ConversionHistoryServiceImpl implements ConversionHistoryService {

	private CurrencyConversionRepository repository;

	@Autowired
	public ConversionHistoryServiceImpl(CurrencyConversionRepository repository) {
		this.repository = repository;
	}

	@Override
	public CurrencyConversion getByConversionDateAndTransactionId(LocalDate conversionDate, String transactionId) {
		return repository.findByConversionDateBetweenAndTransactionId(conversionDate.atStartOfDay(),
				getEndOfDate(conversionDate), transactionId).orElseThrow();
	}

	@Override
	public List<CurrencyConversion> listByConversionDate(LocalDate conversionDate, Pageable pageable) {
		return repository
				.findByConversionDateBetween(conversionDate.atStartOfDay(), getEndOfDate(conversionDate), pageable)
				.getContent();
	}

	@Override
	public CurrencyConversion getByTransactionId(String transactionId) {
		return repository.findByTransactionId(transactionId).orElseThrow();
	}

	@Override
	public CurrencyConversion save(CurrencyConversion currencyConversion) {
		return repository.save(currencyConversion);
	}

	private LocalDateTime getEndOfDate(LocalDate date) {
		return date.atTime(LocalTime.MAX);
	}

}
