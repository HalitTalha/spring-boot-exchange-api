package com.talha.openpayd.exchange.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.talha.openpayd.exchange.model.CurrencyConversion;
import com.talha.openpayd.exchange.model.CurrencyListResult;
import com.talha.openpayd.exchange.model.CurrencyPair;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD) // to recreate mock db before each test
class CurrencyServiceIT {

	@Autowired
	private CurrencyService currencyService;

	@Autowired
	@Qualifier("free-crrconv")
	private RestTemplate restTemplate;

	@Value("${free-crrconv.api-key}")
	private String apiKey;

	@Value("${free-crrconv.endpoint}")
	private String endPointAddress;

	private MockRestServiceServer mockServer;

	@BeforeEach
	public void setUp() {
		mockServer = MockRestServiceServer.createServer(restTemplate);
	}

	@Test
	public void convertCurrencyTest_thenSuccess() throws Exception {
		String rate = "{\"query\":{\"count\":1},\"results\":{\"USD_TRY\":{\"id\":\"USD_TRY\",\"val\":48.435014,\"to\":\"TRY\",\"fr\":\"USD\"}}}";
		mockServer
				.expect(ExpectedCount.once(),
						requestTo(new URI(endPointAddress + "/convert?q=USD_TRY&apiKey=" + apiKey)))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(rate));
		CurrencyConversion conversion = currencyService.convertCurrency(createCurrencyPair(), 1d);
		mockServer.verify();
		assertTrue(conversion.getTargetAmount().equals(48.435014));
	}

	@Test
	public void getCurrenciesTest_thenSuccess() throws Exception {
		String currencies = "{\"results\":{\"ALL\":{\"currencyName\":\"Albanian Lek\",\"currencySymbol\":\"Lek\",\"id\":\"ALL\"},\"XCD\":{\"currencyName\":\"East Caribbean Dollar\",\"currencySymbol\":\"$\",\"id\":\"XCD\"}}}";
		mockServer.expect(ExpectedCount.once(), requestTo(new URI(endPointAddress + "/currencies?apiKey=" + apiKey)))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(currencies));
		CurrencyListResult result = currencyService.getCurrencies();
		mockServer.verify();
		assertTrue(createCurrencyListResult().getCurrencyCodes().containsAll(result.getCurrencyCodes()));
	}

	private CurrencyPair createCurrencyPair() {
		return new CurrencyPair("USD", "TRY");
	}

	private CurrencyListResult createCurrencyListResult() {
		CurrencyListResult result = new CurrencyListResult();
		List<String> list = new ArrayList<String>();
		list.add("ALL");
		list.add("XCD");
		result.setCurrencyCodes(list);
		return result;
	}

}
