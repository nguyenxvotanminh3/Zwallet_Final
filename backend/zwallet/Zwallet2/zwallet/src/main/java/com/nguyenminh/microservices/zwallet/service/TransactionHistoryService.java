package com.nguyenminh.microservices.zwallet.service;

import com.nguyenminh.microservices.zwallet.dto.TransactionHistoryResponse;
import com.nguyenminh.microservices.zwallet.dto.UserResponse;
import com.nguyenminh.microservices.zwallet.model.TransactionHistory;
import com.nguyenminh.microservices.zwallet.model.UserModel;
import com.nguyenminh.microservices.zwallet.repository.TransactionHistoryRepository;
import com.nguyenminh.microservices.zwallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TransactionHistoryService {
    @Autowired
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final UserRepository userRepository;

    public List<TransactionHistoryResponse> getAllTransactionHistory() {
        List<TransactionHistory> transactionHistory = transactionHistoryRepository.findAll();
        return transactionHistory.stream().map(this::mapToTransactionResponse).toList();
    }
    @CacheEvict( value = "userCache", key = "#result.userName")
    public UserResponse createTransactionHistory(String name, TransactionHistory transactionHistory1) {

        UserModel userModel = userRepository.findByUserName(name);

        if (userModel != null) {
            transactionHistory1.setUser(userModel);
            transactionHistoryRepository.save(transactionHistory1);
            return mapToUserResponse(userModel);
        } else {
            throw new RuntimeException("No user found");
        }
    }

    public UserResponse mapToUserResponse(UserModel userModel) {
        List<TransactionHistoryResponse> transactionHistoryResponses = userModel.getTransactionHistory().stream()
                .map(this::mapToTransactionResponse)
                .toList();

        return UserResponse.builder()
                .userId(userModel.getUserId())
                .userName(userModel.getUserName())
                .emailAddress(userModel.getEmailAddress())
                .password(userModel.getPassword())
                .fullName(userModel.getFullName())
                .address(userModel.getAddress())
                .city(userModel.getCity())
                .country(userModel.getCountry())
                .postalCode(userModel.getPostalCode())
                .aboutMe(userModel.getAboutMe())
                .quotes(userModel.getQuotes())
                .tag(userModel.getTag())
                .totalAmount(userModel.getTotalAmount())
                .transactionHistoryResponses(transactionHistoryResponses)
                .build();
    }

    public TransactionHistoryResponse mapToTransactionResponse(TransactionHistory transactionHistory) {
        return TransactionHistoryResponse.builder()
                .transactionId(transactionHistory.getTransactionId())
                .amountUsed(transactionHistory.getAmountUsed())
                .localDateTime(transactionHistory.getLocalDateTime())
                .purpose(transactionHistory.getPurpose())
                .moneyLeft(transactionHistory.getMoneyLeft())
                .userId(transactionHistory.getUser().getUserId())
                .build();
    }


}
