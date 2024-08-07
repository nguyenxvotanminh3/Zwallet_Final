package com.nguyenminh.microservices.currency_exchange_service.repository;

import com.nguyenminh.microservices.currency_exchange_service.model.CurrencyExchange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyExchangeRepository extends JpaRepository<CurrencyExchange, Long> {
    CurrencyExchange findByFromAndTo(String from, String to);
}
