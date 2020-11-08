package com.talha.openpayd.exchange.annotation;

import static com.talha.openpayd.exchange.exception.Constants.INVALID_CURRENCY_MSG;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.talha.openpayd.exchange.beanvalidation.CurrencyCodeValidator;

@Documented
@Constraint(validatedBy = CurrencyCodeValidator.class)
@NotNull
@Size(min = 3, max = 3)
@ReportAsSingleViolation
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCurrencyCode {
	String message() default INVALID_CURRENCY_MSG;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}