package com.talha.openpayd.exchange.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.talha.openpayd.exchange.config.CacheConfig;
import com.talha.openpayd.exchange.model.CurrencyListResult;
import com.talha.openpayd.exchange.model.CurrencyPair;
import com.talha.openpayd.exchange.model.Rate;

@Service
public class CurrencyServiceImpl extends CurrencyService {

	private RestTemplate restTemplate;

	private UriComponentsBuilder convert = UriComponentsBuilder.fromUriString("/convert");

	private String currenciesUri = UriComponentsBuilder.fromUriString("/currencies").toUriString();

	@Autowired
	public CurrencyServiceImpl(ConversionHistoryService conversionHistoryService,
			@Qualifier("free-crrconv") RestTemplate restTemplate) {
		super(conversionHistoryService);
		this.restTemplate = restTemplate;
	}

	@Override
	public Rate getRate(CurrencyPair currencyPair) throws Exception {
		return restTemplate.getForObject(addParameterToUri(currencyPair), Rate.class);
	}

	@Override
	@Cacheable(value = { CacheConfig.CURRENCY_LIST_CACHE })
	public CurrencyListResult getCurrencies() {
		return restTemplate.getForObject(currenciesUri, CurrencyListResult.class);
	}

	private String addParameterToUri(CurrencyPair currencyPair) {
		return convert.queryParam("q", currencyPair.toString()).toUriString();
	}

}
