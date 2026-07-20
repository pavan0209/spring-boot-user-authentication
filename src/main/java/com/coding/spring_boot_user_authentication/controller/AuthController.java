package com.coding.spring_boot_user_authentication.controller;

import com.coding.spring_boot_user_authentication.dto.request.LoginRequest;
import com.coding.spring_boot_user_authentication.dto.request.RegisterRequest;
import com.coding.spring_boot_user_authentication.dto.request.UpdateRequest;
import com.coding.spring_boot_user_authentication.dto.response.ApiResponse;
import com.coding.spring_boot_user_authentication.dto.response.LoginResponse;
import com.coding.spring_boot_user_authentication.dto.response.UserResponse;
import com.coding.spring_boot_user_authentication.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {

    @Autowired
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> registerUser(@Valid @RequestBody RegisterRequest request) {
        UserResponse response = authService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Account created successfully.", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> loginUser(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.loginUser(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Login successful.", response));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateRequest request) {
        UserResponse response = authService.updateUser(request, id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Account updated successfully.", response));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> deleteUser(@PathVariable Long id) {
        authService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Account deleted successfully.", null));
    }
}