package com.nguyenminh.microservices.zwallet.service;

import com.nguyenminh.microservices.zwallet.dto.TransactionHistoryResponse;
import com.nguyenminh.microservices.zwallet.dto.UserRegistrationDto;
import com.nguyenminh.microservices.zwallet.dto.UserResponse;
import com.nguyenminh.microservices.zwallet.model.TransactionHistory;
import com.nguyenminh.microservices.zwallet.model.UserModel;
import com.nguyenminh.microservices.zwallet.repository.TransactionHistoryRepository;
import com.nguyenminh.microservices.zwallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RequiredArgsConstructor
@Service
public class UserModelService {

    private final UserRepository userRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;

    public List<UserResponse> getAllUser() {
        List<UserModel> userModels = userRepository.findAll();
        return userModels.stream().map(this::mapToUserResponse).toList();
    }

    public Optional<UserResponse> getUserById(int id) {
        Optional<UserModel> userModel = userRepository.findById(id);
        if (userModel.isPresent()) {
            return userModel.map(this::mapToUserResponse);
        } else {
            throw new RuntimeException("Cant find user with id: " + id);
        }
    }

    public UserResponse updateUserDetail(String name, UserModel userModel2) {
        UserModel userModel = userRepository.findByUserName(name);

        if (userModel != null) {
            userModel.setUserName(userModel2.getUserName());
            userModel.setCompany(userModel2.getCompany());
            userModel.setCity(userModel2.getCity());
            userModel.setAddress(userModel2.getAddress());
            userModel.setCountry(userModel2.getCountry());
            userModel.setTag(userModel2.getTag());
            userModel.setAboutMe(userModel2.getAboutMe());
            userModel.setQuotes(userModel2.getQuotes());
            userModel.setPostalCode(userModel2.getPostalCode());
            userModel.setEmailAddress(userModel2.getEmailAddress());
            userModel.setFullName(userModel2.getFullName());
            userRepository.save(userModel);
            return mapToUserResponse(userModel);
        } else {
            throw new RuntimeException("Cant find user");
        }
    }

    public UserModel createUser(UserRegistrationDto userRegistrationDto) {
        UserModel userModel = new UserModel();
        userModel.setFullName(userRegistrationDto.getFullName());
        userModel.setEmailAddress(userRegistrationDto.getEmailAddress());
        userModel.setUserName(userRegistrationDto.getUserName());
        userModel.setPassword(userRegistrationDto.getPassword());
        userModel.setCity(null);
        userModel.setAddress(null);
        userModel.setCountry(null);
        userModel.setTag(null);
        userModel.setAboutMe(null);
        userModel.setQuotes(null);
        userModel.setTag(null);
        userModel.setPostalCode(null);
        userModel.setCompany(null);
        userModel.setTotalAmount("0");
        if (userRepository.findByUserName(userRegistrationDto.getUserName()) != null) {
            throw new RuntimeException("This user name has been used!");
        } else {
            userRepository.save(userModel);
            return userModel;
        }
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

    public UserResponse mapToUserResponse(UserModel userModel) {
        List<TransactionHistoryResponse> transactionHistoryResponses = userModel.getTransactionHistory().stream()
                .map(this::mapToTransactionResponse)
                .toList();

        return UserResponse.builder()
                .userId(userModel.getUserId())
                .company(userModel.getCompany())
                .password(userModel.getPassword())
                .userName(userModel.getUserName())
                .emailAddress(userModel.getEmailAddress())
                .fullName(userModel.getFullName())
                .address(userModel.getAddress())
                .city(userModel.getCity())
                .country(userModel.getCountry())
                .postalCode(userModel.getPostalCode())
                .aboutMe(userModel.getAboutMe())
                .quotes(userModel.getQuotes())
                .tag(userModel.getTag())
                .profileImage(userModel.getProfileImage())
                .totalAmount(userModel.getTotalAmount())
                .transactionHistoryResponses(transactionHistoryResponses)
                .build();
    }

    public ResponseEntity<?> checkUserName(Map<String, String> body) {
        String username = body.get("userName");
        boolean exists = userRepository.findByUserName(username) != null;
        return ResponseEntity.ok(Collections.singletonMap("available", !exists));
    }

    @Transactional
    public UserResponse getUserByUserName(String userName) {
        UserModel userModel = userRepository.findByUserName(userName);
        if (userModel != null) {
            List<TransactionHistory> transactionHistories = userModel.getTransactionHistory();

            return UserResponse.builder()
                    .userId(userModel.getUserId())
                    .company(userModel.getCompany())
                    .password(userModel.getPassword())
                    .quotes(userModel.getQuotes())
                    .country(userModel.getCountry())
                    .postalCode(userModel.getPostalCode())
                    .userName(userModel.getUserName())
                    .city(userModel.getCity())
                    .address(userModel.getAddress())
                    .aboutMe(userModel.getAboutMe())
                    .tag(userModel.getTag())
                    .emailAddress(userModel.getEmailAddress())
                    .fullName(userModel.getFullName())
                    .totalAmount(userModel.getTotalAmount())

                    .transactionHistoryResponses(transactionHistories.stream().map(this::mapToTransactionResponse).toList())
                    .build();
        } else {
            throw new RuntimeException("Cant find user with userName : " + userName);
        }
    }

    public String deleteUserByName(String userName) {
        UserModel userModel = userRepository.findByUserName(userName);
        if (userModel != null) {
            userRepository.deleteById(userModel.getUserId());
            return "Deleted user: " + userName;
        } else return "Cant find user: " + userName;
    }

    public UserResponse updateUserTotal(String name, String totalAmount) {
        UserModel userModel = userRepository.findByUserName(name);
        if (userModel != null) {
            userModel.setTotalAmount(totalAmount.toString());
            userRepository.save(userModel);
        } else {
            throw new RuntimeException("Cant find user");
        }
        return mapToUserResponse(userModel);
    }

    public void uploadProfileImage(String name, MultipartFile file) throws IOException {
        UserModel user = userRepository.findByUserName(name);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        String contentType = file.getContentType();
        if (!contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Invalid file type. Only images allowed.");
        }

        byte[] imageBytes = file.getBytes();

        String imageFileName = generateUniqueFileName(contentType);
        saveImageToFile(imageBytes, imageFileName);
        user.setProfileImage(imageFileName.getBytes());

        userRepository.save(user);
        System.out.println("Profile image uploaded successfully for user: " + name);
    }

    private String generateUniqueFileName(String contentType) {
        String extension = getExtension(contentType);
        return UUID.randomUUID().toString() + "." + extension;
    }

    private String getExtension(String contentType) {
        switch (contentType) {
            case "image/jpeg":
                return "jpg";
            case "image/png":
                return "png";
            case "image/gif":
                return "gif";
            default:
                throw new IllegalArgumentException("Unsupported image type");
        }
    }

    private void saveImageToFile(byte[] imageBytes, String fileName) throws IOException {
        Path imagePath = Paths.get("uploads/", fileName);
        Files.createDirectories(imagePath.getParent());
        Files.write(imagePath, imageBytes);
    }

    public byte[] getProfileImage(String name) {
        UserModel user = userRepository.findByUserName(name);
        return user.getProfileImage();
    }
}
