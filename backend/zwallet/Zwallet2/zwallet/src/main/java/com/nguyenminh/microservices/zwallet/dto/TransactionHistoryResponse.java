package com.nguyenminh.microservices.zwallet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistoryResponse {
    private int transactionId;
    private String amountUsed;
    private String localDateTime;
    private String moneyLeft;
    private String purpose;
    private int userId;
}
