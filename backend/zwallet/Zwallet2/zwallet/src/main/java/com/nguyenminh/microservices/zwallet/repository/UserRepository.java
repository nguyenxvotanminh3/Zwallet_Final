package com.nguyenminh.microservices.zwallet.repository;


import com.nguyenminh.microservices.zwallet.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel,Integer> {

    UserModel findByUserName(String userName);
}
