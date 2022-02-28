package org.pkfrc.core.repo.security;

import java.util.List;

import org.pkfrc.core.entities.security.LocalJwt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ILocalJwtRepository extends JpaRepository<LocalJwt, Long>{

	List<LocalJwt> findByUserId(Long id);
}
