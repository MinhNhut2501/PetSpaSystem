package com.petspa.common_service.advice;

import java.util.List;

public record RestViolation(
        String field,
        List<String> messages
) {}
