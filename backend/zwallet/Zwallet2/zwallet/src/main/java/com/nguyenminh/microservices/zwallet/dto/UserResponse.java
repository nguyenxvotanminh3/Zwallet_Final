package com.nguyenminh.microservices.zwallet.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse implements Serializable {
    private int userId;
    private String userName;
    @JsonIgnoreProperties
    private String password;
    private String company;
    private String emailAddress;
    private String fullName;
    private String address;
    private String city;
    private String country;
    private String postalCode;
    private String aboutMe;
    private String quotes;
    private String tag;
    private String totalAmount;

    @Lob
    private byte[] profileImage;
    private List<TransactionHistoryResponse> transactionHistoryResponses;
}
