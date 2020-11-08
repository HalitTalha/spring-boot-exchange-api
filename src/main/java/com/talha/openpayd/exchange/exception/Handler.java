package com.talha.openpayd.exchange.exception;

import java.util.NoSuchElementException;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

import com.talha.openpayd.exchange.model.ErrorMessage;

@RestControllerAdvice
public class Handler {

	private Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

	@ExceptionHandler({ Exception.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorMessage handleException(Exception e) {
		logger.error(getErrorOpenningTag(ErrorCode.GENERIC_ERROR.getCode()), e);
		return new ErrorMessage(ErrorCode.GENERIC_ERROR.getMsg(), ErrorCode.GENERIC_ERROR.getCode());
	}

	@ExceptionHandler({ NoSuchElementException.class })
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public ErrorMessage handleNoCurrencyConversionException(NoSuchElementException e) {
		logger.error(getErrorOpenningTag(ErrorCode.NO_RECORDS_FOUND.getCode()), e);
		return new ErrorMessage(e.getMessage(), ErrorCode.NO_RECORDS_FOUND.getCode());
	}

	@ExceptionHandler({ HttpServerErrorException.class, HttpServerErrorException.ServiceUnavailable.class })
	@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
	public ErrorMessage handleServiceUnavailableException(Exception e) {
		logger.error(getErrorOpenningTag(ErrorCode.SERVICE_UNAVAILABLE.getCode()), e);
		return new ErrorMessage(ErrorCode.SERVICE_UNAVAILABLE.getMsg(), ErrorCode.SERVICE_UNAVAILABLE.getCode());
	}

	@ExceptionHandler({ MissingServletRequestParameterException.class,
			UnsatisfiedServletRequestParameterException.class, BindException.class,
			ConstraintViolationException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ErrorMessage handleInvalidParameterExceptions(Exception e) {
		logger.error(getErrorOpenningTag(ErrorCode.INVALID_PARAMETERS.getCode()), e);
		return new ErrorMessage(ErrorCode.INVALID_PARAMETERS.getMsg(), ErrorCode.INVALID_PARAMETERS.getCode());
	}

	private String getErrorOpenningTag(int errorCode) {
		return "<ERROR_CODE_" + errorCode + ">";
	}
}
