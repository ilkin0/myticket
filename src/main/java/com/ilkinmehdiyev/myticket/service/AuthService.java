package com.ilkinmehdiyev.myticket.service;

import com.ilkinmehdiyev.myticket.domain.dto.RegisterRequest;
import com.ilkinmehdiyev.myticket.domain.dto.RegisterResponse;
import com.ilkinmehdiyev.myticket.domain.dto.SignInRequest;
import com.ilkinmehdiyev.myticket.domain.dto.SignInResponse;
import com.ilkinmehdiyev.myticket.domain.entity.Role;
import com.ilkinmehdiyev.myticket.domain.entity.Token;
import com.ilkinmehdiyev.myticket.domain.entity.User;
import com.ilkinmehdiyev.myticket.domain.enums.UserRole;
import com.ilkinmehdiyev.myticket.exceptions.DataNotFoundException;
import com.ilkinmehdiyev.myticket.exceptions.RegistrationException;
import com.ilkinmehdiyev.myticket.repo.RoleRepository;
import com.ilkinmehdiyev.myticket.repo.TokenRepository;
import com.ilkinmehdiyev.myticket.security.JwtService;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserService userService;
  private final PasswordEncoder encoder;
  private final RoleRepository roleRepository;
  private final JwtService jwtService;
  private final TokenRepository tokenRepository;
  private final AuthenticationManager authenticationManager;

  @Value("${security.jwt.accessToken.expiration}")
  private long accessTokenExpiration;

  public RegisterResponse registerUser(@Valid RegisterRequest request) {
    ///  1. receive data
    String email = request.email();
    log.debug("Registering user, username : {}", email);

    //    2. Validation data
    if (userService.existsByEmail(email)) {
      log.debug("User already exists, username : {}", email);
      throw new RegistrationException("User already exists");
    }

    //    3. create Data
    Role role = getRoleByName(UserRole.USER);
    var user =
        User.builder()
            .email(email)
            .firstName(request.firstName())
            .lastName(request.lastName())
            .password(encoder.encode(request.password()))
            .roles(Set.of(role))
            .isAccountNonExpired(true)
            .isAccountNonLocked(true)
            .isCredentialsNonExpired(true)
            .build();

    user.setIsActive(true);

    ///  4. write to DB
    User savedUser = userService.save(user);
    log.info("User {} registered successfully", email);
    return new RegisterResponse(email);
  }

  private Role getRoleByName(UserRole role) {
    return roleRepository
        .findByName(role)
        .orElseThrow(
            () -> {
              log.error("Could not find role: {}", role);
              return new DataNotFoundException("Could not find role: %s".formatted(role));
            });
  }

  public SignInResponse signinUser(SignInRequest request) {
    log.debug("Starting to SignIn user: {}", request.email());

    try {
      var authenticationToken =
          new UsernamePasswordAuthenticationToken(request.email(), request.password());
      Authentication authenticate = authenticationManager.authenticate(authenticationToken);
      SecurityContextHolder.getContext().setAuthentication(authenticate);
    } catch (Exception e) {
      log.error("Username and/or password is incorrect");
      throw new RuntimeException("Username and/or password is incorrect", e);
    }

    String accessToken = jwtService.generateAccessToken(request.email());
    String refreshToken = jwtService.generateRefreshToken(request.email());
    persistAccessToken(request, accessToken);

    return new SignInResponse(accessToken, refreshToken);
  }

  private void persistAccessToken(SignInRequest request, String accessToken) {
    var user = userService.findByUsername(request.email());
    Token token =
        Token.builder()
            .value(accessToken)
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(accessTokenExpiration))
            .user(user)
            .build();
    token.setValue(accessToken);

    tokenRepository.save(token);
  }
}
