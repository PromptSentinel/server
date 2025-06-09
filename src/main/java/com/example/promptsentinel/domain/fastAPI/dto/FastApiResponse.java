package com.example.promptsentinel.domain.fastAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class FastApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> FastApiResponse<T> success(String message, T data) {
        return new FastApiResponse<>(true, message, data);
    }

    public static <T> FastApiResponse<T> error(String message) {
        return new FastApiResponse<>(false, message, null);
    }
}