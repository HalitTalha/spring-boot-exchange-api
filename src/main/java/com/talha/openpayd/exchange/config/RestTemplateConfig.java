package com.talha.openpayd.exchange.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.talha.openpayd.exchange.util.AddApiKeyInterceptor;

@Configuration
public class RestTemplateConfig {

	private long readTimeOut;

	private long connectionTimeOut;

	private String currencyProviderEndpoint;

	public RestTemplateConfig(@Value("${free-crrconv.readTimeOut}") long readTimeOut,
			@Value("${free-crrconv.connectionTimeOut}") long connectionTimeOut,
			@Value("${free-crrconv.endpoint}") String currencyProviderEndpoint) {
		this.readTimeOut = readTimeOut;
		this.connectionTimeOut = connectionTimeOut;
		this.currencyProviderEndpoint = currencyProviderEndpoint;
	}

	@Bean("free-crrconv")
	public RestTemplate restTemplate(RestTemplateBuilder builder, AddApiKeyInterceptor addApiKeyInterceptor) {
		return builder.setReadTimeout(Duration.ofMillis(readTimeOut))
				.setConnectTimeout(Duration.ofMillis(connectionTimeOut)).rootUri(currencyProviderEndpoint)
				.interceptors(addApiKeyInterceptor).build();
	}

}
