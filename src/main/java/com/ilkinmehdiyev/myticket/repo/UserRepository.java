package com.ilkinmehdiyev.myticket.repo;

import com.ilkinmehdiyev.myticket.domain.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);

  Boolean existsByEmail(String email);
}
