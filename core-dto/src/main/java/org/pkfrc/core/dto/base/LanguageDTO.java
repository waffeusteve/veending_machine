package org.pkfrc.core.dto.base;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.pkfrc.core.entities.base.Language;
import org.pkfrc.core.utilities.enumerations.EWriteMode;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class LanguageDTO extends AbstractParamDTO<Language, Long> {

	private static final long serialVersionUID = 1L;

	private EWriteMode mode = EWriteMode.ltr;
	private String locale;
	private String flag;
	private boolean prefered;
	private boolean active;
	public Set<TranslationDTO> translations = new HashSet<>(0);


	public LanguageDTO(Language entity) {
		mapper.map(entity, this);
		this.translations = TranslationDTO.getTranslations(entity.getTranslations());
	}

	@Override
	public Language toEntity(Language entity) {
		mapper.map(this, entity);
		entity.setTranslations(TranslationDTO.getTranslations(translations, entity.getTranslations()));
		return entity;
	}



}
