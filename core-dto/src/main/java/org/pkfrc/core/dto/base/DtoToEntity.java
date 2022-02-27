package org.pkfrc.core.dto.base;


import java.io.Serializable;

public interface DtoToEntity<E  extends Serializable> extends  Serializable {

    E toEntity(E entity);
}
