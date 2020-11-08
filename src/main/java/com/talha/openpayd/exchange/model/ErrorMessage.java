package com.talha.openpayd.exchange.model;

public class ErrorMessage {

	private String detail;

	private Integer responseCode;

	public ErrorMessage(String detail, Integer responseCode) {
		super();
		this.detail = detail;
		this.responseCode = responseCode;
	}

	public Integer getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(Integer responseCode) {
		this.responseCode = responseCode;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

}
