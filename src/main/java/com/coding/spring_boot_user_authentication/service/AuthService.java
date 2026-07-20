package com.coding.spring_boot_user_authentication.service;

import com.coding.spring_boot_user_authentication.dto.request.LoginRequest;
import com.coding.spring_boot_user_authentication.dto.request.RegisterRequest;
import com.coding.spring_boot_user_authentication.dto.request.UpdateRequest;
import com.coding.spring_boot_user_authentication.dto.response.LoginResponse;
import com.coding.spring_boot_user_authentication.dto.response.UserResponse;

public interface AuthService {

    UserResponse registerUser(RegisterRequest request);

    LoginResponse loginUser(LoginRequest request);

    UserResponse updateUser(UpdateRequest request, Long id);

    void deleteUser(Long id);

}