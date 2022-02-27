package org.pkfrc.core.dto.security;


import lombok.Data;
import org.pkfrc.core.entities.security.Fonctionalite;
import org.pkfrc.core.entities.security.Profile;

import java.util.ArrayList;
import java.util.List;
@Data
public class ProfileDTO extends ProfileResumeDTO {
	private static final long serialVersionUID = 1L;



	private Integer pwdDuration;
	private Integer unameDuration;
	private String type;
	private Integer maxSessions;
	private Integer maxConnectAttempts;
	private String defaultPwd;
	private Boolean autoValidated = Boolean.FALSE;
	private Boolean selfCreated = Boolean.FALSE;
	private Boolean sendMail = Boolean.FALSE;
	private Boolean validateByMail = Boolean.FALSE;
	private Integer pwdMinLength = 0;
	private Integer pwdMinNbNumeric = 0;
	private Integer pwdMinNbSpecialChar = 0;
	private Integer pwdMinNbUpperChar = 0;

	private List<FonctionaliteDTO> fonctionalites = new ArrayList<>(0);

	public ProfileDTO() {
		super();
	}

	public ProfileDTO(Profile entity) {
		super(entity);
		if (entity.getFonctionalites() != null) {
			this.fonctionalites.clear();
			for (Fonctionalite f : entity.getFonctionalites()) {
				fonctionalites.add(new FonctionaliteDTO(f));
			}
		}
		this.maxSessions = entity.getMaxSessions();
		this.maxConnectAttempts = entity.getMaxConnectAttempts();
		this.defaultPwd = entity.getDefaultPwd();
		this.autoValidated = entity.getAutoValidated();
		this.selfCreated = entity.getSelfCreated();
		this.sendMail = entity.getSendMail();
		this.validateByMail = entity.getValidateByMail();
		this.pwdMinLength = entity.getPwdMinLength();
		this.pwdMinNbNumeric = entity.getPwdMinNbNumeric();
		this.pwdMinNbSpecialChar = entity.getPwdMinNbSpecialChar();
		this.pwdMinNbUpperChar = entity.getPwdMinNbUpperChar();
	}

	@Override
	public Profile toEntity(Profile entity) {
		entity = super.toEntity(entity);
		if (this.fonctionalites != null && !this.fonctionalites.isEmpty()) {
			entity.getFonctionalites().clear();
			for (FonctionaliteDTO service : this.fonctionalites) {
				entity.getFonctionalites().add(service.toEntity(new Fonctionalite()));
			}
		}
		entity.setMaxSessions(maxSessions);
		entity.setMaxConnectAttempts(maxConnectAttempts);
		entity.setDefaultPwd(defaultPwd);
		entity.setAutoValidated(autoValidated);
		entity.setSelfCreated(selfCreated);
		entity.setSendMail(sendMail);
		entity.setValidateByMail(validateByMail);
		entity.setPwdMinLength(pwdMinLength);
		entity.setPwdMinNbNumeric(pwdMinNbNumeric);
		entity.setPwdMinNbSpecialChar(pwdMinNbSpecialChar);
		entity.setPwdMinNbUpperChar(pwdMinNbUpperChar);
		return entity;
	}



	public List<FonctionaliteDTO> getFonctionalites() {
		return fonctionalites;
	}

	public void setFonctionalites(List<FonctionaliteDTO> fonctionalites) {
		this.fonctionalites = fonctionalites;
	}
}
