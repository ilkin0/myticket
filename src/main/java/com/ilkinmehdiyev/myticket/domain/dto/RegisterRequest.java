package com.ilkinmehdiyev.myticket.domain.dto;

import jakarta.validation.constraints.NotBlank;

///  TODO email and password regex or annotation?>
public record RegisterRequest(
    @NotBlank String email,
    @NotBlank String password,
    @NotBlank String firstName,
    @NotBlank String lastName) {}
