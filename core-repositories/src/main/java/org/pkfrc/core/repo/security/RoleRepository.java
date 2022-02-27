package org.pkfrc.core.repo.security;

import org.pkfrc.core.entities.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String id);
}
