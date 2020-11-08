package com.talha.openpayd.exchange.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.talha.openpayd.exchange.model.CurrencyConversion;
import com.talha.openpayd.exchange.model.CurrencyListResult;
import com.talha.openpayd.exchange.model.CurrencyPair;
import com.talha.openpayd.exchange.model.Rate;
import com.talha.openpayd.exchange.service.CurrencyService;

@Validated
@RestController
public class CurrencyController {

	private CurrencyService currencyService;

	@Autowired
	public CurrencyController(CurrencyService currencyService) {
		this.currencyService = currencyService;
	}

	@RequestMapping(value = "/currencies")
	public @ResponseBody ResponseEntity<CurrencyListResult> getAvailableCurrencies() {
		return ResponseEntity.ok(currencyService.getCurrencies());
	}

	@RequestMapping(value = "/convert")
	public @ResponseBody ResponseEntity<CurrencyConversion> convert(@Validated CurrencyPair currencyPair,
			@RequestParam Double sourceAmount) throws Exception {
		return ResponseEntity.ok(currencyService.convertCurrency(currencyPair, sourceAmount));
	}

	@RequestMapping(value = "/rate")
	public @ResponseBody ResponseEntity<Rate> getRate(@Validated CurrencyPair currencyPair) throws Exception {
		return ResponseEntity.ok(currencyService.getRate(currencyPair));
	}
}
