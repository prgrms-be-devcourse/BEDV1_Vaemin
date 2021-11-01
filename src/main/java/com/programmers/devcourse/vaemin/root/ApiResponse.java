package com.programmers.devcourse.vaemin.root;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ApiResponse<T> {
    private final T data;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime serverDatetime = LocalDateTime.now();
    private final String message;

    public static <U> ApiResponse<U> success(String message) {
        return success(null, message);
    }
  
    public static <U> ApiResponse<U> success(U result) {
        return success(result, "");
    }
  
    public static <U> ApiResponse<U> success(U result, String message) {
        return new ApiResponse<>(result, message);
    }
    
    public static <U> ApiResponse<U> fail(String message) {
        return fail(null, message);
    }
    
    public static <U> ApiResponse<U> fail(U result) {
        return fail(result, "");
    }
  
    public static <U> ApiResponse<U> fail(U result, String message) {
        return new ApiResponse<>(result, message);
    }
}