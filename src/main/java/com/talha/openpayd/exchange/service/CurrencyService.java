package com.talha.openpayd.exchange.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.talha.openpayd.exchange.model.CurrencyConversion;
import com.talha.openpayd.exchange.model.CurrencyListResult;
import com.talha.openpayd.exchange.model.CurrencyPair;
import com.talha.openpayd.exchange.model.Rate;

public abstract class CurrencyService {

	private ConversionHistoryService conversionHistoryService;

	@Autowired
	public CurrencyService(ConversionHistoryService conversionHistoryService) {
		this.conversionHistoryService = conversionHistoryService;
	}

	public abstract Rate getRate(CurrencyPair currencyPair) throws Exception;

	public abstract CurrencyListResult getCurrencies();

	public CurrencyConversion convertCurrency(CurrencyPair currencyPair, Double sourceAmount) throws Exception {
		Rate rate = getRate(currencyPair);
		Double targetAmount = rate.getValue() * sourceAmount;
		CurrencyConversion conversion = new CurrencyConversion(rate, sourceAmount, targetAmount);
		return saveCurrencyConversion(conversion);
	}

	private CurrencyConversion saveCurrencyConversion(CurrencyConversion conversion) {
//		conversion.setTransactionId(generateTransactionId());
//		conversion.setConversionDate(LocalDateTime.now());
		return conversionHistoryService.save(conversion);
	}

//	private String generateTransactionId() {
//		return UUID.randomUUID().toString();
//	}

}
