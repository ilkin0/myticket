package com.ilkinmehdiyev.myticket.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record SignInRequest(@NotBlank String email, @NotBlank String password) {}
