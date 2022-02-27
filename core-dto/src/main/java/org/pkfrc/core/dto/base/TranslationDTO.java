package org.pkfrc.core.dto.base;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.pkfrc.core.entities.translation.Translation;
@Data
@NoArgsConstructor
public class TranslationDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String desc;
	private String lang;
	private Long id;
	private Long version;

	public TranslationDTO(String lang, String desc, Long id, Long version) {
		super();
		this.lang = lang;
		this.desc = desc;
		this.id = id;
		this.version = version;
	}

	public static Set<TranslationDTO> getTranslations(Set<Translation> translations) {
		Set<TranslationDTO> dtos = new HashSet<>(0);

		for (Translation trans : translations) {
			dtos.add(new TranslationDTO(trans.getLang(), trans.getTranslation(), trans.getId(), trans.getVersion()));
		}
		return dtos;
	}

	public static Set<Translation> getTranslations(Set<TranslationDTO> dtos, Set<Translation> translations) {
		Set<Translation> result = new HashSet<>(0);

		BiFunction<Set<Translation>, TranslationDTO, Translation> getTrans = (trans, lang) -> {
			for (Translation tran : trans) {
				if (tran.getLang().equals(lang.getLang())) {
					tran.setTranslation(lang.getDesc());
					return tran;
				}
			}
			return new Translation(lang.getLang(), lang.getDesc(), lang.getId(), lang.getVersion());
		};

		for (TranslationDTO dto : dtos) {
			result.add(getTrans.apply(translations, dto));
		}
		return result;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lang == null) ? 0 : lang.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TranslationDTO other = (TranslationDTO) obj;
		if (lang == null) {
			if (other.lang != null)
				return false;
		} else if (!lang.equals(other.lang))
			return false;
		return true;
	}

}
