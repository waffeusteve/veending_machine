package org.pkfrc.core.dto.base;

import org.pkfrc.core.entities.base.BaseEntity;

import java.io.Serializable;

/**
 * @author Steve Waffeu
 */
public interface ParamEntityDTO<E extends BaseEntity<ID>, ID extends Serializable> extends EntityDTO<E, ID>{

    String getCode();

    void setCode(String code);

    String getDesignation();

    void setDesignation(String designation);
}
