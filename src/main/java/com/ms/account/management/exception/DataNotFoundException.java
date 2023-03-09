package com.ms.account.management.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class DataNotFoundException extends RuntimeException {

    private final String message;
}
