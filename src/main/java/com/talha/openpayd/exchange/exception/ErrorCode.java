package com.talha.openpayd.exchange.exception;

import static com.talha.openpayd.exchange.exception.Constants.GENERIC_ERROR_MSG;
import static com.talha.openpayd.exchange.exception.Constants.INVALID_PARAMETERS_MSG;
import static com.talha.openpayd.exchange.exception.Constants.NO_RECORD_FOUND_MSG;
import static com.talha.openpayd.exchange.exception.Constants.SERVICE_UNAVAILABLE_MSG;

public enum ErrorCode {
	SERVICE_UNAVAILABLE(101, SERVICE_UNAVAILABLE_MSG), INVALID_PARAMETERS(102, INVALID_PARAMETERS_MSG),
	GENERIC_ERROR(103, GENERIC_ERROR_MSG), NO_RECORDS_FOUND(104, NO_RECORD_FOUND_MSG);

	private final int code;
	private final String msg;

	private ErrorCode(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

}
