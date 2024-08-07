package com.nguyenminh.microservices.zwallet.repository;


import com.nguyenminh.microservices.zwallet.model.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Integer> {
}
