package com.talha.openpayd.exchange.beanvalidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.talha.openpayd.exchange.annotation.ValidCurrencyCode;
import com.talha.openpayd.exchange.service.CurrencyService;

public class CurrencyCodeValidator implements ConstraintValidator<ValidCurrencyCode, String> {

	private CurrencyService currencyService;

	@Autowired
	public CurrencyCodeValidator(CurrencyService currencyService) {
		this.currencyService = currencyService;
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return isSupported(value);
	}

	private boolean isSupported(String value) {
		return currencyService.getCurrencies().getCurrencyCodes().contains(value);
	}

}
