package com.coding.spring_boot_user_authentication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean status;

    private String message;

    private T data;
}
