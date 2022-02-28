package org.pkfrc.core.dto.security;

import org.modelmapper.ModelMapper;
import org.pkfrc.core.dto.base.DtoToEntity;
import org.pkfrc.core.entities.enums.EUserType;
import org.pkfrc.core.entities.security.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDTO implements DtoToEntity<User> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Double deposit;
    String userName;
    String password;
    EUserType type;

    private static ModelMapper mapper = new ModelMapper();
    @Override
    public User toEntity(User entity) {
        mapper.map(this, entity);
        return entity;
    }
}
