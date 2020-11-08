package com.talha.openpayd.exchange.util;

import java.io.IOException;
import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class AddApiKeyInterceptor implements ClientHttpRequestInterceptor {

	private static final String API_KEY_PARAM = "apiKey";

	@Value("${free-crrconv.api-key}")
	private String apiKey;

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		URI uri = UriComponentsBuilder.fromHttpRequest(request).queryParam(API_KEY_PARAM, apiKey).build().toUri();

		HttpRequest modifiedRequest = new HttpRequestWrapper(request) {

			@Override
			public URI getURI() {
				return uri;
			}
		};
		return execution.execute(modifiedRequest, body);
	}

}
