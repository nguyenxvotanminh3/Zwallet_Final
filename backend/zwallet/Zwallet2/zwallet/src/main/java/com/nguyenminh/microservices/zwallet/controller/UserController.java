package com.nguyenminh.microservices.zwallet.controller;
import com.nguyenminh.microservices.zwallet.dto.UserRegistrationDto;
import com.nguyenminh.microservices.zwallet.dto.UserResponse;
import com.nguyenminh.microservices.zwallet.model.UserModel;
import com.nguyenminh.microservices.zwallet.service.UserModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class UserController {

    private final UserModelService userModelService;
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.FOUND)
    public List<UserResponse> getAllUser(){
        return userModelService.getAllUser();
    }
    @GetMapping("/user/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public Optional<UserResponse> getUserById(@PathVariable int id){
        return userModelService.getUserById(id);
    }

    @PutMapping("/user/update/{name}")
    public UserResponse updateUserDetail(@PathVariable String name, @RequestBody UserModel userModel){
        return userModelService.updateUserDetail(name,userModel);
    }

    @PutMapping("/user/update-total-amount/{name}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse updateUserTotal(@PathVariable String name, @RequestBody String totalAmount){
        return userModelService.updateUserTotal(name,totalAmount);
    }



    @PostMapping("/user/create")

    @ResponseStatus(HttpStatus.CREATED)
    public UserModel createUser(@RequestBody UserRegistrationDto userRegistrationDto){

        return userModelService.createUser(userRegistrationDto);
    }

    @PostMapping("/check-user")
    public ResponseEntity<?> checkUsername(@RequestBody Map<String, String> body) {
        return userModelService.checkUserName(body);
    }
    @GetMapping("/{userName}")
//    @Cacheable("user-search")
    public UserResponse getUserByName(@PathVariable String userName){
        return userModelService.getUserByUserName(userName);
    }

    @DeleteMapping("/user/delete/{userName}")
    @ResponseStatus(HttpStatus.GONE)
    public String deleteUser(@PathVariable String userName){
        return userModelService.deleteUserByName(userName);
    }


    @PutMapping("/{name}/uploadProfileImage")
    public ResponseEntity<String> uploadProfileImage(@PathVariable String name, @RequestParam("file") MultipartFile file) throws IOException {
        userModelService.uploadProfileImage(name, file);
        return ResponseEntity.ok("Profile image uploaded successfully");
    }

    @GetMapping("/{name}/profileImage")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable String name) {
        try {
            byte[] imageBytes = userModelService.getProfileImage(name);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // Chọn loại nội dung phù hợp với ảnh
                    .body(imageBytes);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
