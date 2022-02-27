package org.pkfrc.core.repo.security;

import java.util.Optional;

import org.pkfrc.core.entities.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);

    Optional<User> findByUserName(String username);

    User findByUserNameIgnoreCase(String username);

}