package com.ilkinmehdiyev.myticket.service;

import com.ilkinmehdiyev.myticket.domain.entity.User;
import com.ilkinmehdiyev.myticket.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  public User findByUsername(String username) {
    return userRepository
        .findByEmail(username)
        .orElseThrow(
            () -> {
              log.error("Username {} not found", username);
              return new UsernameNotFoundException(username);
            });
  }
}