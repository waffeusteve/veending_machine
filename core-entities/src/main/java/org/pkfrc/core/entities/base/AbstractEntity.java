package org.pkfrc.core.entities.base;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import lombok.Data;

@Data
@MappedSuperclass
public abstract class AbstractEntity<ID extends Serializable> implements BaseEntity<ID> {

    private static final long serialVersionUID = 1L;

    @Version
    protected Long version;

    protected String userCreate;

    @Temporal(TemporalType.TIMESTAMP)
    protected Date dateCreate;

    protected String userUpdate;

    @Temporal(TemporalType.TIMESTAMP)
    protected Date dateUpdate;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractEntity)) return false;
        AbstractEntity<?> that = (AbstractEntity<?>) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
