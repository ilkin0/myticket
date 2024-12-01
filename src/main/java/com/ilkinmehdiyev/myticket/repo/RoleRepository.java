package com.ilkinmehdiyev.myticket.repo;

import com.ilkinmehdiyev.myticket.domain.entity.Role;
import com.ilkinmehdiyev.myticket.domain.enums.UserRole;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(UserRole name);
}
