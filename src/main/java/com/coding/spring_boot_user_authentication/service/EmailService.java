package com.coding.spring_boot_user_authentication.service;

public interface EmailService {

    void sendSetPasswordEmail(String to, String name, String token);

}