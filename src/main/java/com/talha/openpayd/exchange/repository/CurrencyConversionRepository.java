package com.talha.openpayd.exchange.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.talha.openpayd.exchange.model.CurrencyConversion;

@Repository
public interface CurrencyConversionRepository extends PagingAndSortingRepository<CurrencyConversion, Long> {

	Optional<CurrencyConversion> findByConversionDateBetweenAndTransactionId(LocalDateTime conversionDate,
			LocalDateTime conversionDateEnd, String transactionId);

	Page<CurrencyConversion> findByConversionDateBetween(LocalDateTime conversionDate, LocalDateTime conversionDateEnd,
			Pageable pageable);

	Optional<CurrencyConversion> findByTransactionId(String transactionId);

}
