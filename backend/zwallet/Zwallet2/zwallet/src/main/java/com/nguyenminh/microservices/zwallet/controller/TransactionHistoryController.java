package com.nguyenminh.microservices.zwallet.controller;
import com.nguyenminh.microservices.zwallet.dto.TransactionHistoryResponse;
import com.nguyenminh.microservices.zwallet.dto.UserResponse;
import com.nguyenminh.microservices.zwallet.model.TransactionHistory;
import com.nguyenminh.microservices.zwallet.service.TransactionHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2")
@RequiredArgsConstructor
public class TransactionHistoryController {
    @Autowired
    private TransactionHistoryService transactionHistoryService;

    @GetMapping("/all")
    public List<TransactionHistoryResponse> getAllTransactionHistory (){
        return transactionHistoryService.getAllTransactionHistory();
    }

    @PostMapping("/trans/create/{name}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse createTransactionHistory (@PathVariable String name , @RequestBody TransactionHistory transactionHistory){
        return transactionHistoryService.createTransactionHistory(name,transactionHistory);
    }
}
