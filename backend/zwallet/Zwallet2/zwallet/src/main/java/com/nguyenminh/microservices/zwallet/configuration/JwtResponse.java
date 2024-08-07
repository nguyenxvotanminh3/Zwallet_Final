package com.nguyenminh.microservices.zwallet.configuration;

public class JwtResponse {
    private String token; // JWT
    private String type = "Bearer"; // Loại token (thường là "Bearer")
    private String userName; // Tên người dùng (tùy chọn)
    private String roles; // Các vai trò của người dùng (tùy chọn)



    public JwtResponse(String accessToken, String userName, String roles) {
        this.token = accessToken;
        this.userName = userName;
        this.roles = roles;
    }

    // Getters
    public String getToken() {
        return token;
    }

    public String getType() {
        return type;
    }

    public String getUsername() {
        return userName;
    }

    public String getRoles() {
        return roles;
    }
}
