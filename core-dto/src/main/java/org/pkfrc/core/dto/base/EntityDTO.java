package org.pkfrc.core.dto.base;

import org.pkfrc.core.entities.base.BaseEntity;

import java.io.Serializable;
/**
 * @author Steve Waffeu
 */
public interface EntityDTO<E extends BaseEntity<ID>, ID extends Serializable> extends  DtoToEntity<E>{

    ID getId();

    void setId(ID id);

    Long getVersion();

    void setVersion(Long version);

    default EntityDTO<E, ID>  fromEntity(E entity){
        setId(entity.getId());
        setVersion(entity.getVersion());
        return this;
    }
    default E toEntity(E entity){
        entity.setId(getId());
        entity.setVersion(getVersion());
        return entity;
    }

}
