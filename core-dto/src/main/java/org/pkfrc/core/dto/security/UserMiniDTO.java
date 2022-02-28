package org.pkfrc.core.dto.security;

import java.io.Serializable;

import org.modelmapper.ModelMapper;
import org.pkfrc.core.entities.enums.EUserType;
import org.pkfrc.core.entities.security.User;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserMiniDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private EUserType type;
	private static ModelMapper mapper = new ModelMapper();

	public UserMiniDTO(User entity) {
		mapper.map(entity, this);
	}

	
	public User toEntity(User entity) {
		mapper.map(this, entity);
		return entity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EUserType getType() {
		return type;
	}

	public void setType(EUserType type) {
		this.type = type;
	}


}
