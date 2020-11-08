package com.talha.openpayd.exchange.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "CURRENCY_CONVERSION", uniqueConstraints = { @UniqueConstraint(columnNames = { "id", "source", "target",
		"value", "sourceAmount", "targetAmount", "transactionId", "conversionDate" }),
		@UniqueConstraint(columnNames = { "transactionId" }) })
@SequenceGenerator(name = "sq_generator", sequenceName = "currency_conversion_seq")
@EntityListeners(AuditingEntityListener.class)
public class CurrencyConversion {

	@Id
	@GeneratedValue
	@JsonIgnore
	private long id;

	@Version
	@JsonIgnore
	private int version;

	@Embedded
	private Rate rate;
	private Double sourceAmount;
	private Double targetAmount;

	private String transactionId;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(columnDefinition = "TIMESTAMP")
	@CreatedDate
	private LocalDateTime conversionDate;

	public CurrencyConversion() {
		super();
	}

	@PrePersist
	public void addTransactionId() {
		transactionId = UUID.randomUUID().toString();
	}

	public CurrencyConversion(Rate rate, Double sourceAmount, Double targetAmount) {
		super();
		this.rate = rate;
		this.sourceAmount = sourceAmount;
		this.targetAmount = targetAmount;
	}

	public Rate getRate() {
		return rate;
	}

	public void setRate(Rate rate) {
		this.rate = rate;
	}

	public Double getSourceAmount() {
		return sourceAmount;
	}

	public void setSourceAmount(Double sourceAmount) {
		this.sourceAmount = sourceAmount;
	}

	public LocalDateTime getConversionDate() {
		return conversionDate;
	}

	public Double getTargetAmount() {
		return targetAmount;
	}

	public void setTargetAmount(Double targetAmount) {
		this.targetAmount = targetAmount;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public long getId() {
		return id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(conversionDate, rate, sourceAmount, targetAmount, transactionId, version);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CurrencyConversion other = (CurrencyConversion) obj;
		return Objects.equals(conversionDate, other.conversionDate) && Objects.equals(rate, other.rate)
				&& Objects.equals(sourceAmount, other.sourceAmount) && Objects.equals(targetAmount, other.targetAmount)
				&& Objects.equals(transactionId, other.transactionId) && version == other.version;
	}

}
