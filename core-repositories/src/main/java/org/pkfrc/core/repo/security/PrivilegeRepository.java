package org.pkfrc.core.repo.security;

import org.pkfrc.core.entities.security.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    Optional<Privilege> findByName(String id);
}