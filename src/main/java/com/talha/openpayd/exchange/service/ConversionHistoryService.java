package com.talha.openpayd.exchange.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.talha.openpayd.exchange.model.CurrencyConversion;

public interface ConversionHistoryService {

	CurrencyConversion getByConversionDateAndTransactionId(LocalDate conversionDate, String transactionId);

	List<CurrencyConversion> listByConversionDate(LocalDate date, Pageable pageable);

	CurrencyConversion getByTransactionId(String transactionId);

	CurrencyConversion save(CurrencyConversion currencyConversion);

}
