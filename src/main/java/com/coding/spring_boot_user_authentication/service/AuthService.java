package com.coding.spring_boot_user_authentication.service;

import com.coding.spring_boot_user_authentication.dto.request.*;
import com.coding.spring_boot_user_authentication.dto.response.ApiResponse;
import com.coding.spring_boot_user_authentication.dto.response.LoginResponse;
import com.coding.spring_boot_user_authentication.dto.response.UserResponse;

public interface AuthService {

    ApiResponse<Void> registerUser(RegisterRequest request);

    ApiResponse<Void> setPassword(SetPasswordRequest request);

    ApiResponse<Void> forgotPassword(ForgotPasswordRequest request);

    ApiResponse<Void> resetPassword(ResetPasswordRequest request);

    LoginResponse loginUser(LoginRequest request);

    UserResponse updateUser(UpdateRequest request, Long id);

    void deleteUser(Long id);

    UserResponse getProfile(String email);
}