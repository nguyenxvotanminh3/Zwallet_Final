package com.nguyenminh.microservices.currency_exchange_service.service;

import com.nguyenminh.microservices.currency_exchange_service.model.CurrencyExchange;
import com.nguyenminh.microservices.currency_exchange_service.repository.CurrencyExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurrencyExchangeService {
    @Autowired
    private final CurrencyExchangeRepository currencyExchangeRepository;

    public CurrencyExchangeService(CurrencyExchangeRepository currencyExchangeRepository) {
        this.currencyExchangeRepository = currencyExchangeRepository;
    }

    public List<CurrencyExchange> showCurrencyExchangeTable() {
        List<CurrencyExchange> currencyExchanges = currencyExchangeRepository.findAll();
        return currencyExchanges;
    }
    public Optional<CurrencyExchange> updateCurrencyExchangeTable(Long id,CurrencyExchange currencyExchange) {
        Optional<CurrencyExchange> currencyExchanges = currencyExchangeRepository.findById(id);
        currencyExchanges.ifPresent(currencyExchange1 -> {
            currencyExchange1.setId(currencyExchange.getId());
            currencyExchange1.setEnvironment(currencyExchange.getEnvironment());
            currencyExchange1.setConversionMultiple(currencyExchange.getConversionMultiple());
            currencyExchange1.setFrom(currencyExchange.getFrom());
            currencyExchange1.setTo(currencyExchange1.getTo());
            currencyExchangeRepository.save(currencyExchange1);
        });
        return currencyExchanges;
    }

    public CurrencyExchange createCurrencyExchangeTable(CurrencyExchange currencyExchange) {
        CurrencyExchange currencyExchange1 = new CurrencyExchange();
        currencyExchange1.setId(currencyExchange.getId());
        currencyExchange1.setTo(currencyExchange.getTo());
        currencyExchange1.setFrom(currencyExchange.getFrom());
        currencyExchange1.setEnvironment(currencyExchange.getEnvironment());
        currencyExchange1.setConversionMultiple(currencyExchange.getConversionMultiple());
        currencyExchangeRepository.save(currencyExchange1);
        return currencyExchange1;
    }


}
