package org.pkfrc.core.dto.security;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.pkfrc.core.dto.base.AbstractDTO;
import org.pkfrc.core.dto.base.AbstractParamDTO;
import org.pkfrc.core.entities.security.Profile;
import org.pkfrc.core.entities.security.User;

@Data
public class ProfileResumeDTO extends AbstractDTO<Profile,Long> {
	private static final long serialVersionUID = 1L;

	public ProfileResumeDTO() {
		super();
	}

	private String code;
	private String intitule;
	private Integer pwdDuration;
	private Integer unameDuration;
	private String type;




	public ProfileResumeDTO(Profile entity) {
//		super(entity);
		this.id = entity.getId();
		this.pwdDuration = entity.getPwdDuration();
		this.code = entity.getCode();
		this.intitule = entity.getIntitule();
		this.unameDuration = entity.getUnameDuration();
		this.type = entity.getType();

	}

	@Override
	public Profile toEntity(Profile entity) {
		entity = super.toEntity(entity);
		entity.setPwdDuration(pwdDuration);
		entity.setUnameDuration(unameDuration);
		entity.setCode(code);
		entity.setIntitule(intitule);
		entity.setType(type);
		return entity;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Profile)) return false;
		return Objects.equals(code, code) &&
				Objects.equals(intitule, intitule);
	}

	@Override
	public int hashCode() {
		return Objects.hash(code, intitule);
	}


}
