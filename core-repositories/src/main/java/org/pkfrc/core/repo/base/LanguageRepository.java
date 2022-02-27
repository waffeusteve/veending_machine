package org.pkfrc.core.repo.base;

import org.pkfrc.core.entities.base.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Long> {

    Language findByCode(String code);
}
