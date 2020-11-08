package com.talha.openpayd.exchange.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.HttpServerErrorException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.talha.openpayd.exchange.exception.ErrorCode;
import com.talha.openpayd.exchange.model.CurrencyConversion;
import com.talha.openpayd.exchange.model.CurrencyListResult;
import com.talha.openpayd.exchange.model.CurrencyPair;
import com.talha.openpayd.exchange.model.Rate;
import com.talha.openpayd.exchange.service.ConversionHistoryService;
import com.talha.openpayd.exchange.service.CurrencyService;

@ExtendWith({ RestDocumentationExtension.class, SpringExtension.class })
@WebMvcTest(CurrencyController.class)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
class CurrencyControllerIT {

	@MockBean
	private CurrencyService currencyService;

	@MockBean
	private ConversionHistoryService conversionHistoryService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		when(currencyService.getCurrencies()).thenReturn(createCurrencyListResult());
	}

	@Test
	void getRateTest_WithValidInputs_ThenStatusOk() throws Exception {
		when(currencyService.getRate(createCurrencyPair())).thenReturn(createRate());
		mockMvc.perform(get("/rate?source=TRY&target=USD")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andDo(createRateDocsForValidInputs());
	}

	private RestDocumentationResultHandler createRateDocsForValidInputs() {
		return document("rate",
				responseFields(fieldWithPath("currencyPair.source").description("The source currency"),
						fieldWithPath("currencyPair.target").description("The target currency"),
						fieldWithPath("value").description("The value of the rate")));
	}

	@Test
	void getRateTest_WithInvalidTargetCurrency_ThenBadRequest() throws Exception {
		mockMvc.perform(get("/rate?source=TRY&target=US")).andDo(createErrorDocs("currency-400")).andExpect(status().isBadRequest());
	}

	@Test
	void getRateTest_ThenServiceUnavailable() throws Exception {
		when(currencyService.getRate(new CurrencyPair("XYZ", "ZYX")))
				.thenThrow(new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE));
		mockMvc.perform(get("/rate?source=XYZ&target=ZYX")).andDo(createErrorDocs("currency-503"))
				.andExpect(status().isServiceUnavailable());
	}

	private RestDocumentationResultHandler createErrorDocs(String docName) {
		return document(docName, responseFields(
				fieldWithPath("detail").description("A global description of the cause of the error"),
				fieldWithPath("responseCode").description("Application error code. One of " + ErrorCode.values())));
	}

	@Test
	void getRateTest_ThenInternalServerError() throws Exception {
		when(currencyService.getRate(new CurrencyPair("ABC", "CBA"))).thenThrow(new Exception());
		mockMvc.perform(get("/rate?source=ABC&target=CBA")).andDo(createErrorDocs("currency-500")).andExpect(status().isInternalServerError());
	}

	@Test
	void getRateTest_WithUnSupportedCurrency_ThenBadRequest() throws Exception {
		mockMvc.perform(get("/rate?source=TRY&target=YYY")).andExpect(status().isBadRequest());

	}

	@Test
	void convertTest_WithValidInputs_ThenStatusOk() throws Exception {
		when(currencyService.convertCurrency(createCurrencyPair(), 1.2d)).thenReturn(createCurrencyConversion());
		mockMvc.perform(get("/convert?source=TRY&target=USD&sourceAmount=1.2")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andDo(createConvertDocsForValidInputs());
	}

	private RestDocumentationResultHandler createConvertDocsForValidInputs() {
		return document("currency-convert",
				responseFields(fieldWithPath("rate.currencyPair.source").description("The source currency"),
						fieldWithPath("rate.currencyPair.target").description("The target currency"),
						fieldWithPath("rate.value").description("The value of the rate"),
						fieldWithPath("sourceAmount").description("The source amount in source currency"),
						fieldWithPath("targetAmount").description("The converted amount in target currency"),
						fieldWithPath("transactionId").description("Unique transaction UID"),
						fieldWithPath("conversionDate").description("Date of the transaction")));
	}

	@Test
	void convertTest_WithoutAmount_ThenBadRequest() throws Exception {
		mockMvc.perform(get("/convert?source=TRY&target=USD")).andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	void getAvailableCurrenciesTest_thenSuccess() throws Exception {
		mockMvc.perform(get("/currencies")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andDo(createDocsForAvailableCurrencies());
	}

	private RestDocumentationResultHandler createDocsForAvailableCurrencies() {
		return document("currencies", responseFields(
				fieldWithPath("currencyCodes").description("Array containing all the available currency codes")));
	}

	@Test
	void getRateTest_WithValidInput_ReturnsRate() throws Exception {
		when(currencyService.getRate(createCurrencyPair())).thenReturn(createRate());
		MvcResult mvcResult = mockMvc.perform(get("/rate?source=TRY&target=USD")).andReturn();
		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		String expectedResponseBody = objectMapper.writeValueAsString(createRate());
		assertEquals(expectedResponseBody, actualResponseBody);
	}

	@Test
	void convertTest_WithValidInput_ReturnsCurrencyConverison() throws Exception {
		when(currencyService.convertCurrency(createCurrencyPair(), 1.2d)).thenReturn(createCurrencyConversion());
		MvcResult mvcResult = mockMvc.perform(get("/convert?source=TRY&target=USD&sourceAmount=1.2")).andReturn();
		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		String expectedResponseBody = objectMapper.writeValueAsString(createCurrencyConversion());
		assertEquals(expectedResponseBody, actualResponseBody);
	}

	@Test
	void getAvailableCurrenciesTest_WithValidInput_ReturnsCurrencyListResult() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/currencies")).andReturn();
		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		String expectedResponseBody = objectMapper.writeValueAsString(createCurrencyListResult());
		assertEquals(expectedResponseBody, actualResponseBody);
	}

	private CurrencyListResult createCurrencyListResult() {
		CurrencyListResult result = new CurrencyListResult();
		List<String> list = new ArrayList<String>();
		list.add("USD");
		list.add("TRY");
		list.add("XYZ");
		list.add("ZYX");
		list.add("ABC");
		list.add("CBA");
		result.setCurrencyCodes(list);
		return result;
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
