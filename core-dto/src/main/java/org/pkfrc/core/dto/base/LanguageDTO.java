package org.pkfrc.core.dto.base;

import org.pkfrc.core.entities.base.Language;
import org.pkfrc.core.utilities.enumerations.EWriteMode;

import lombok.NoArgsConstructor;


@NoArgsConstructor
public class LanguageDTO extends AbstractParamDTO<Language, Long> {

	private static final long serialVersionUID = 1L;

	private EWriteMode mode = EWriteMode.ltr;
	private String locale;
	private String flag;
	private boolean prefered;
	private boolean active;
	


	public LanguageDTO(Language entity) {
		mapper.map(entity, this);
	}

	@Override
	public Language toEntity(Language entity) {
		mapper.map(this, entity);		
		return entity;
	}

	public EWriteMode getMode() {
		return mode;
	}

	public void setMode(EWriteMode mode) {
		this.mode = mode;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public boolean isPrefered() {
		return prefered;
	}

	public void setPrefered(boolean prefered) {
		this.prefered = prefered;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}



}
