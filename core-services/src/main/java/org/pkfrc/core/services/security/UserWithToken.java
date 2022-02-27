package org.pkfrc.core.services.security;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.pkfrc.core.entities.enums.EUserStatus;
import org.pkfrc.core.entities.enums.EUserType;
import org.pkfrc.core.entities.security.Role;
import org.pkfrc.core.entities.security.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserWithToken implements Serializable {
	private String name;
	private EUserType type;
	private String token;
	private Boolean defaultPwd = Boolean.FALSE;
	private Boolean sessionUp = Boolean.FALSE;

	public UserWithToken(User user) {
		this.name = user.getUserName();

	}

	public UserWithToken(User user, String token) {
		this.name = user.getUserName();
		this.token = token;
		this.type = user.getType();
		this.defaultPwd = user.getDefaultPwd();
		this.sessionUp = user.getSessionUp();

	}
}
