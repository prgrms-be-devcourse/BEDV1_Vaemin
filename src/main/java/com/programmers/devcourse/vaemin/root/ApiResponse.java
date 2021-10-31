package com.programmers.devcourse.vaemin.root;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class ApiResponse<T> {
    private final boolean success;
    private final T result;
    private final String error;

    public static <U> ApiResponse<U> success(U result) {
        return new ApiResponse<>(true, result, "");
    }

    // question : 임의로 만든건데 result값을 null로 하는 게 낫나
    public static <U> ApiResponse<U> success() {
        return new ApiResponse<U>(true, null, HttpStatus.NO_CONTENT.getReasonPhrase());
    }

    public static <U> ApiResponse<U> fail(U result, String message) {
        return new ApiResponse<>(false, result, message);
    }

    public static <U> ApiResponse<U> fail(String message) {
        return fail(null, message);
    }
}