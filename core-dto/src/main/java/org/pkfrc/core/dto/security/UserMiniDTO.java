package org.pkfrc.core.dto.security;

import org.pkfrc.core.dto.base.AbstractDTO;
import org.pkfrc.core.dto.base.EntityDTO;
import org.pkfrc.core.entities.enums.EUserType;
import org.pkfrc.core.entities.security.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;


@NoArgsConstructor
@Data
public class UserMiniDTO extends AbstractDTO<User, Long> implements EntityDTO<User, Long> {
  private String name;
  private EUserType type;
  private String login;
  private String company;
  private static ModelMapper mapper = new ModelMapper();


  public UserMiniDTO(User entity) {
    mapper.map(entity, this);
  }

  @Override
  public User toEntity(User entity) {
    mapper.map(this, entity);
    return entity;
  }
}
