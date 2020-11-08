package com.talha.openpayd.exchange.api;

import java.time.LocalDate;

import javax.validation.constraints.PastOrPresent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.talha.openpayd.exchange.model.CurrencyConversion;
import com.talha.openpayd.exchange.service.ConversionHistoryService;

@RequestMapping(value = "/conversion-history")
@RestController
@Validated
public class ConversionHistoryController {

	private ConversionHistoryService conversionHistoryService;

	@Autowired
	public ConversionHistoryController(ConversionHistoryService conversionHistoryService) {
		this.conversionHistoryService = conversionHistoryService;
	}

	@RequestMapping(params = { "date" })
	public @ResponseBody ResponseEntity<Iterable<CurrencyConversion>> getHistory(
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PastOrPresent LocalDate date, Pageable pageable)
			throws Exception {
		return ResponseEntity.ok(conversionHistoryService.listByConversionDate(date, pageable));
	}

	@RequestMapping(params = { "transactionId" })
	public @ResponseBody ResponseEntity<CurrencyConversion> getHistory(String transactionId, Pageable pageable)
			throws Exception {
		return ResponseEntity.ok(conversionHistoryService.getByTransactionId(transactionId));
	}

	@RequestMapping(params = { "transactionId", "date" })
	public @ResponseBody ResponseEntity<CurrencyConversion> getHistory(String transactionId,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PastOrPresent LocalDate date, Pageable pageable)
			throws Exception {
		return ResponseEntity.ok(conversionHistoryService.getByConversionDateAndTransactionId(date, transactionId));
	}

}
