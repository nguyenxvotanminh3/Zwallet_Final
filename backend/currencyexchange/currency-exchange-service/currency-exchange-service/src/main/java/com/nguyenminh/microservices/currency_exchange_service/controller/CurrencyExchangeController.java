package com.nguyenminh.microservices.currency_exchange_service.controller;

import com.nguyenminh.microservices.currency_exchange_service.model.CurrencyExchange;
import com.nguyenminh.microservices.currency_exchange_service.repository.CurrencyExchangeRepository;
import com.nguyenminh.microservices.currency_exchange_service.service.CurrencyExchangeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController

public class CurrencyExchangeController {
    private Logger logger = LoggerFactory.getLogger(CurrencyExchange.class);
    @Autowired
    private CurrencyExchangeRepository currencyExchangeRepository;
    @Autowired
    private CurrencyExchangeService currencyExchangeService;
    @Autowired
    private Environment environment;
    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    @ResponseStatus(HttpStatus.OK)
    public CurrencyExchange retriveExchangeValue(@PathVariable
                                                 String from ,
                                                 @PathVariable
                                                 String to)
    {      //2024-07-07T22:19:38.655+07:00  INFO [currency-exchange,a115bf1fab56fe3f605e4578d9a390cc,e2ebaef929766523]  21556 --- [currency-exchange] [nio-8000-exec-1] [a115bf1fab56fe3f605e4578d9a390cc-e2ebaef929766523] c.n.m.c.model.CurrencyExchange           : Currency exchangeg from USD to INR
        logger.info("Currency exchangeg from {} to {}" , from , to);
//        CurrencyExchange currencyExchange = new CurrencyExchange(1000L, from, to, BigDecimal.valueOf(50));
        CurrencyExchange currencyExchange = currencyExchangeRepository.findByFromAndTo(from, to);
        if(currencyExchange== null){
            throw new RuntimeException("Unable to find data for " + from + " to "+ to);
        }
        String port = environment.getProperty("local.server.port");
        currencyExchange.setEnvironment(port);

        return currencyExchange;
    }


}
