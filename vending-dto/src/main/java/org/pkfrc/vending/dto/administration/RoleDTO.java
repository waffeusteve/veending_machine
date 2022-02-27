package org.pkfrc.vending.dto.administration;

import java.io.Serializable;

import org.pkfrc.core.dto.base.AbstractDTO;
import org.pkfrc.core.entities.security.Role;

import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
public class RoleDTO extends AbstractDTO<Role, Long> implements Serializable{

	private static final long serialVersionUID = 1L;
    
    private String name;

    public RoleDTO(Role entity) {
        super(entity);
        this.name = entity.getName();	
    }

    @Override
    public Role toEntity(Role entity) {
        entity = super.toEntity(entity);
        entity.setName(name);
		
		return entity;
    }
}
