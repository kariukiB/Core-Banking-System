package com.kariuki.banking.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response <T> {
    private T entity;
    private String message;
    private Integer statusCode;
}
