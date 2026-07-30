package com.coding.spring_boot_user_authentication.service;

import com.coding.spring_boot_user_authentication.dto.request.*;
import com.coding.spring_boot_user_authentication.dto.response.ApiResponse;
import com.coding.spring_boot_user_authentication.dto.response.LoginResponse;
import com.coding.spring_boot_user_authentication.dto.response.UserResponse;
import com.coding.spring_boot_user_authentication.entity.User;
import com.coding.spring_boot_user_authentication.entity.VerificationToken;
import com.coding.spring_boot_user_authentication.exception.InvalidCredentialsException;
import com.coding.spring_boot_user_authentication.exception.InvalidTokenException;
import com.coding.spring_boot_user_authentication.exception.UserAlreadyExistsException;
import com.coding.spring_boot_user_authentication.exception.UserNotFoundException;
import com.coding.spring_boot_user_authentication.repository.UserRepository;
import com.coding.spring_boot_user_authentication.repository.VerificationTokenRepository;
import com.coding.spring_boot_user_authentication.security.JwtService;
import com.coding.spring_boot_user_authentication.security.TokenType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final VerificationTokenRepository verificationTokenRepository;

    private final EmailService emailService;

    @Override
    @Transactional
    public ApiResponse<Void> registerUser(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("User already exists.");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(null)
                .enabled(false)
                .build();

        user = userRepository.save(user);
        String token = jwtService.generatePasswordSetupToken(user);

        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .type(TokenType.PASSWORD_SETUP)
                .expiryDate(LocalDateTime.now().plusMinutes(30))
                .used(false)
                .user(user)
                .build();

        verificationTokenRepository.save(verificationToken);
        emailService.sendSetPasswordEmail(user.getEmail(), user.getFirstName(), token);

        return new ApiResponse<>(true, "Registration successful. Please check your email to set your password.", null);
    }

    @Override
    @Transactional
    public ApiResponse<Void> resetPassword(ResetPasswordRequest request) {

        VerificationToken verificationToken =
                verificationTokenRepository.findByTokenAndType(request.getToken(), TokenType.PASSWORD_RESET)
                        .orElseThrow(() -> new RuntimeException("Invalid reset token"));

        if (verificationToken.getUsed()) {
            throw new InvalidTokenException("This reset password link has already been used.");
        }

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException(("Reset link expired"));
        }

        if (!jwtService.validatePasswordResetToken(request.getToken())) {
            throw new InvalidTokenException("Invalid or expired reset password token.");
        }

        User user = verificationToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        verificationToken.setUsed(true);
        verificationTokenRepository.save(verificationToken);

        return new ApiResponse<>(true, "Password updated succesfully", null);
    }

    @Override
    @Transactional
    public ApiResponse<Void> setPassword(SetPasswordRequest request) {

        VerificationToken verificationToken = verificationTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new InvalidTokenException("Invalid verification token."));

        if (verificationToken.getUsed()) {
            throw new RuntimeException("This link has already been used.");
        }

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Verification link has expired.");
        }

        if (!jwtService.validatePasswordSetupToken(request.getToken())) {
            throw new InvalidTokenException("Invalid or expired password setup link.");
        }

        User user = verificationToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);

        userRepository.save(user);

        verificationToken.setUsed(true);
        verificationTokenRepository.save(verificationToken);

        return new ApiResponse<>(true, "Password created successfully.", null);
    }

    @Override
    @Transactional
    public ApiResponse<Void> forgotPassword(ForgotPasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String token = jwtService.generatePasswordResetToken(user);

        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .type(TokenType.PASSWORD_RESET)
                .expiryDate(LocalDateTime.now().plusMinutes(30))
                .used(false)
                .user(user)
                .build();

        verificationTokenRepository.save(verificationToken);
        emailService.sendResetPasswordEmail(user.getEmail(), user.getFirstName(), token);

        return new ApiResponse<>(true, "Password reset link send successfully", null);
    }

    @Override
    public LoginResponse loginUser(LoginRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password."));

        String token = jwtService.generateToken(user.getEmail());

        return LoginResponse.builder()
                .token(token)
                .user(UserResponse.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .phoneNumber(user.getPhoneNumber())
                        .build())
                .build();
    }

    @Override
    public UserResponse updateUser(UpdateRequest request, Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());

        User updatedUser = userRepository.save(user);

        return UserResponse.builder()
                .id(updatedUser.getId())
                .firstName(updatedUser.getFirstName())
                .lastName(updatedUser.getLastName())
                .email(updatedUser.getEmail())
                .phoneNumber(updatedUser.getPhoneNumber())
                .build();
    }

    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        userRepository.delete(user);
    }

    @Override
    public UserResponse getProfile(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}