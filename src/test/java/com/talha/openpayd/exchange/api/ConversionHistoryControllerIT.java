package com.talha.openpayd.exchange.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.talha.openpayd.exchange.exception.ErrorCode;
import com.talha.openpayd.exchange.model.CurrencyConversion;
import com.talha.openpayd.exchange.model.CurrencyPair;
import com.talha.openpayd.exchange.model.Rate;
import com.talha.openpayd.exchange.service.ConversionHistoryService;

@ExtendWith({ RestDocumentationExtension.class, SpringExtension.class })
@WebMvcTest(ConversionHistoryController.class)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
class ConversionHistoryControllerIT {

	@MockBean
	private ConversionHistoryService conversionHistoryService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void getHistoryTest_WithNotExistingTransactionId_ThenNotFound() throws Exception {
		when(conversionHistoryService.getByTransactionId("WRONGTRANSACTIONID")).thenThrow(new NoSuchElementException("WRONGTRANSACTIONID is not found"));
		mockMvc.perform(get("/conversion-history?transactionId=WRONGTRANSACTIONID")).andDo(createErrorDocs("conversion-404"))
				.andExpect(status().isNotFound());
	}
	
	@Test
	void getHistoryTest_ThenInternalServerError() throws Exception {
		when(conversionHistoryService.getByTransactionId("SOMETRANSACTIONID")).thenThrow(new NullPointerException());
		mockMvc.perform(get("/conversion-history?transactionId=SOMETRANSACTIONID")).andDo(createErrorDocs("conversion-500"))
				.andExpect(status().isInternalServerError());
	}
	
	@Test
	void getHistoryTest_ThenBadRequest() throws Exception {
		mockMvc.perform(get("/conversion-history")).andDo(createErrorDocs("conversion-400"))
				.andExpect(status().isBadRequest());
	}
	
	private RestDocumentationResultHandler createErrorDocs(String docName) {
		return document(docName, responseFields(
				fieldWithPath("detail").description("A global description of the cause of the error"),
				fieldWithPath("responseCode").description("Application error code. One of " + ErrorCode.values())));
	}

	@Test
	void getHistoryTest_WithValidTransactionId_ReturnsCurrencyConverison() throws Exception {
		CurrencyConversion conversionResult = createCurrencyConversion();
		when(conversionHistoryService.getByTransactionId("TESTTRANSACTIONID")).thenReturn(conversionResult);
		ReflectionTestUtils.setField(conversionResult, "conversionDate", createLocalDateTime("2020-10-16 00:00"));
		MvcResult mvcResult = mockMvc.perform(get("/conversion-history?transactionId=TESTTRANSACTIONID"))
				.andDo(createDocsForValidListResponse()).andReturn();
		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		String expectedResponseBody = objectMapper.writeValueAsString(conversionResult);
		assertEquals(expectedResponseBody, actualResponseBody);
	}

	@Test
	void getHistoryTest_WithValidConversionDateAndTransactionId_ReturnsCurrencyConverison() throws Exception {
		CurrencyConversion conversionResult = createCurrencyConversion();
		LocalDateTime conversionDateTime = createLocalDateTime("2020-10-16 00:00");
		when(conversionHistoryService.getByConversionDateAndTransactionId(conversionDateTime.toLocalDate(),
				"TESTTRANSACTIONID")).thenReturn(conversionResult);
		ReflectionTestUtils.setField(conversionResult, "conversionDate", createLocalDateTime("2020-10-16 00:00"));
		MvcResult mvcResult = mockMvc
				.perform(get("/conversion-history?transactionId=TESTTRANSACTIONID&date=2020-10-16"))
				.andDo(createDocsForValidListResponse()).andReturn();
		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		String expectedResponseBody = objectMapper.writeValueAsString(conversionResult);
		assertEquals(expectedResponseBody, actualResponseBody);
	}

	private RestDocumentationResultHandler createDocsForValidListResponse() {
		return document("conversion-history",
				responseFields(fieldWithPath("rate.currencyPair.source").description("The source currency"),
						fieldWithPath("rate.currencyPair.target").description("The target currency"),
						fieldWithPath("rate.value").description("The value of the rate"),
						fieldWithPath("sourceAmount").description("The source amount in source currency"),
						fieldWithPath("targetAmount").description("The converted amount in target currency"),
						fieldWithPath("transactionId").description("Unique transaction UID"),
						fieldWithPath("conversionDate").description("Date of the transaction")));
	}

	@Test
	void getHistoryTest_WithValidConversionDate_ReturnsCurrencyConverisonList() throws Exception {
		CurrencyConversion conversionResult = createCurrencyConversion();
		LocalDateTime conversionDateTime = createLocalDateTime("2020-10-16 00:00");
		ReflectionTestUtils.setField(conversionResult, "conversionDate", conversionDateTime);
		List<CurrencyConversion> list = new ArrayList<CurrencyConversion>();
		list.add(conversionResult);
		when(conversionHistoryService.listByConversionDate(eq(conversionDateTime.toLocalDate()),
				any(PageRequest.class))).thenReturn(list);
		MvcResult mvcResult = mockMvc.perform(get("/conversion-history?date=2020-10-16&page=0&size=10"))
				.andDo(createDocsForValidResponse()).andReturn();
		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		String expectedResponseBody = objectMapper.writeValueAsString(list);
		assertEquals(expectedResponseBody, actualResponseBody);
	}

	private RestDocumentationResultHandler createDocsForValidResponse() {
		return document("conversion-history",
				responseFields(fieldWithPath("[0].rate.currencyPair.source").description("The source currency"),
						fieldWithPath("[0].rate.currencyPair.target").description("The target currency"),
						fieldWithPath("[0].rate.value").description("The value of the rate"),
						fieldWithPath("[0].sourceAmount").description("The source amount in source currency"),
						fieldWithPath("[0].targetAmount").description("The converted amount in target currency"),
						fieldWithPath("[0].transactionId").description("Unique transaction UID"),
						fieldWithPath("[0].conversionDate").description("Date of the transaction")));
	}

	private LocalDateTime createLocalDateTime(String date) {
		return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
	}

	private CurrencyConversion createCurrencyConversion() {
		CurrencyConversion conversion = new CurrencyConversion();
		conversion.setRate(createRate());
		conversion.setSourceAmount(1.2d);
		conversion.setTargetAmount(1.2d);
		return conversion;
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
}
