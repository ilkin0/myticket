package com.ilkinmehdiyev.myticket.repo;

import com.ilkinmehdiyev.myticket.domain.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
  Optional<Token> findByValue(String token);
}
