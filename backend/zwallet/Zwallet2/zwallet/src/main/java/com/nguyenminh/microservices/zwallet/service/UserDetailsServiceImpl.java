package com.nguyenminh.microservices.zwallet.service;
import com.nguyenminh.microservices.zwallet.dto.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserModelService userModelService;


    public UserDetailsServiceImpl(UserModelService userModelService) {
        this.userModelService = userModelService;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserResponse user = userModelService.getUserByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        log.info("Found user: {}", user);
        log.info("Password: {}", user.getPassword()); // Log the raw password

        return User.builder()
                .username(user.getUserName())
                .password(user.getPassword())
                .authorities("USER")
                .build();
    }

}