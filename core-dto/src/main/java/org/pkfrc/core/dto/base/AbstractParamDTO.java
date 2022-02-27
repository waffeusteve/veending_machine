package org.pkfrc.core.dto.base;

import org.pkfrc.core.entities.base.ParamEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Ulrich lele
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AbstractParamDTO <E extends ParamEntity<ID>, ID extends Serializable> extends AbstractDTO<E, ID> implements ParamEntityDTO<E, ID> {
    protected String code;
    protected String designation;

    public AbstractParamDTO(E entity){
        super(entity);
        code = entity.getCode();
        designation = entity.getDesignation();
    }

    @Override
    public  E toEntity(E entity) {
        entity = super.toEntity(entity);
        entity.setCode(code);
        entity.setDesignation(designation);
        return entity;
    }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof AbstractParamDTO<?, ?>)) return false;
    if (!super.equals(o)) return false;
    AbstractParamDTO<?, ?> that = (AbstractParamDTO<?, ?>) o;
    return Objects.equals(code, that.code);
  }

  @Override
  public int hashCode() {

    return Objects.hash(super.hashCode(), code);
  }
}
