package com.ilkinmehdiyev.myticket.controller;

import com.ilkinmehdiyev.myticket.domain.dto.RegisterRequest;
import com.ilkinmehdiyev.myticket.domain.dto.RegisterResponse;
import com.ilkinmehdiyev.myticket.domain.dto.SignInRequest;
import com.ilkinmehdiyev.myticket.domain.dto.SignInResponse;
import com.ilkinmehdiyev.myticket.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest request) {
    log.info("Request received for user registration");

    var registerResponse = authService.registerUser(request);
    return new ResponseEntity<>(registerResponse, HttpStatus.CREATED);
  }

  @PostMapping("/signin")
  public ResponseEntity<SignInResponse> signInUser(@RequestBody @Valid SignInRequest request) {
    log.info("Request received for user signin: {}", request.email());

    var signInResponse = authService.signinUser(request);
    return new ResponseEntity<>(signInResponse, HttpStatus.OK);
  }
  //          - SignOut
  //          - ForgetPassword ?

}
