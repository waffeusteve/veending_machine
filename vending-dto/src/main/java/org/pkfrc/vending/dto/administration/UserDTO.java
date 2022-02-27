package org.pkfrc.vending.dto.administration;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.pkfrc.core.dto.base.AbstractDTO;
import org.pkfrc.core.entities.enums.EUserStatus;
import org.pkfrc.core.entities.enums.EUserType;
import org.pkfrc.core.entities.security.User;

import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
public class UserDTO extends AbstractDTO<User, Long> implements Serializable{

	private static final long serialVersionUID = 1L;

	
	private String userName;
	private String password;
    private EUserType type;
    private EUserStatus status; 
    private Boolean sessionUp = Boolean.FALSE;
    private Double deposit;
    private Set<RoleDTO> roles = new HashSet<>();

	public UserDTO(User entity) {
		super(entity);
		mapper.map(entity, this);
	}

	@Override
	public User toEntity(User entity) {
		entity = super.toEntity(entity);
		mapper.map(this, entity);
		return entity;
	}
}
