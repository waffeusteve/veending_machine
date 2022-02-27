package org.pkfrc.core.repo.refgen;

import java.util.List;

import org.pkfrc.core.entities.refgen.Reference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReferenceRepository extends JpaRepository<Reference, Long> {
	List<Reference> findByClazz(String clazz);

	Reference findByCode(String code);
}
