package com.programmers.devcourse.vaemin.root;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ApiResponse<T> {

    private int statusCode;
    private T data;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime serverDatetime;

    private final boolean success;
    private final T result;
    private final String error;

    public static <U> ApiResponse<U> success(U result) {
        return new ApiResponse<>(true, result, "");
    }

    public static <U> ApiResponse<U> fail(U result, String message) {
        return new ApiResponse<>(false, result, message);
    }

    public static <U> ApiResponse<U> fail(String message) {
        return fail(null, message);
    }

    public ApiResponse(int statusCode, T data) {
        this.statusCode = statusCode;
        this.data = data;
        this.serverDatetime = LocalDateTime.now();
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(200, data);
    }

    public static <T> ApiResponse<T> fail(int statusCode, T data) {
        return new ApiResponse<>(statusCode, data);
    }
}
}