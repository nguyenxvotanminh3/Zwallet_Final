package com.nguyenminh.microservices.currency_exchange_service.controller;

import com.nguyenminh.microservices.currency_exchange_service.service.CurrencyExchangeService;
import com.nguyenminh.microservices.currency_exchange_service.model.CurrencyExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class changeExchangeRateController {
    @Autowired
    private final CurrencyExchangeService currencyExchangeService;

    public changeExchangeRateController(CurrencyExchangeService currencyExchangeService) {
        this.currencyExchangeService = currencyExchangeService;
    }


    @GetMapping("/show-table-exchange")
    @ResponseStatus(HttpStatus.OK)
    public List<CurrencyExchange> showCurrencyExchangeTable(){
        return currencyExchangeService.showCurrencyExchangeTable();
    }

//    @GetMapping("/show-table-exchange")
//    @ResponseStatus(HttpStatus.OK)
//    public List<CurrencyExchange> updateCurrencyExchangeTable(){
//        return currencyExchangeService.updateCurrencyExchangeTable();
//    }

    @PutMapping("/update/{id}")
    public Optional<CurrencyExchange> updateCurrencyExchangeTable(@PathVariable Long id , CurrencyExchange currencyExchange){
        return currencyExchangeService.updateCurrencyExchangeTable(id,currencyExchange);
    }

    @PostMapping("/create")
    public CurrencyExchange createCurrencyExchangeTable(@RequestBody CurrencyExchange currencyExchange){
        return currencyExchangeService.createCurrencyExchangeTable(currencyExchange);
    }


}
