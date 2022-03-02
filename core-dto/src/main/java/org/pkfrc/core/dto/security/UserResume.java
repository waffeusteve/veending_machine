package org.pkfrc.core.dto.security;

import java.io.Serializable;

import org.modelmapper.ModelMapper;
import org.pkfrc.core.entities.enums.EUserStatus;
import org.pkfrc.core.entities.enums.EUserType;
import org.pkfrc.core.entities.security.User;

import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
public class UserResume  implements Serializable{

	private static final long serialVersionUID = 1L;

	
	private String userName;
    private EUserType type;
    private EUserStatus status;
    private Double deposit;
    
    public static ModelMapper mapper = new ModelMapper();
    
	public UserResume(User entity) {		
		mapper.map(entity, this);
	}

	
}
