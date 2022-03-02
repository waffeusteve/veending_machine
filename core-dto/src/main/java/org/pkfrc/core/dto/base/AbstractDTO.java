package org.pkfrc.core.dto.base;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.pkfrc.core.entities.base.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
* @author Steve Waffeu
 */

@AllArgsConstructor
@NoArgsConstructor
public abstract  class AbstractDTO<E extends BaseEntity<ID>, ID extends Serializable>  implements EntityDTO<E, ID> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected ID id;
    protected Long version;

//    protected String userCreate;
//
//    @Temporal(TemporalType.TIMESTAMP)
//    protected Date dateCreate;
//
//    protected String userUpdate;
//
//    @Temporal(TemporalType.TIMESTAMP)
//    protected Date dateUpdate;

    public AbstractDTO(E entity){
        id = entity.getId();
        version = entity.getVersion();
    }

    @Override
     public  E toEntity(E entity) {
        entity.setId(id);
        entity.setVersion(version);
        return entity;
    }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof AbstractDTO)) return false;
    AbstractDTO<?, ?> that = (AbstractDTO<?, ?>) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {

    return Objects.hash(id);
  }

    public static ModelMapper mapper = new ModelMapper();

    public <D extends AbstractDTO<E, ID>> Set<E> toEntities(Class<E> clazz, Set<D> dtos) {
        return (LinkedHashSet<E>) toEntities(clazz, dtos, new LinkedHashSet<>(0));

    }

    public <D extends AbstractDTO<E, ID>> Collection<E> toEntities(Class<E> clazz, Collection<D> dtos,
                                                                  Collection<E> entities) {
        try {
            Constructor<E> constructor = clazz.getDeclaredConstructor();
            E entity;
            for (D dto : dtos) {
                entity = (E) constructor.newInstance();
                entity = dto.toEntity(entity);

                entities.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entities;
    }

    public <D extends AbstractDTO<E, ID>> List<E> toEntities(Class<E> clazz, List<D> dtos) {
        return (List<E>) toEntities(clazz, dtos, new ArrayList<>(0));
    }

    @SuppressWarnings("unchecked")
    public <D extends AbstractDTO<E, ID>> Collection<D> toDTOs(Class<E> clazz, Collection<D> dtos,
                                                              Collection<E> entities) {
        try {
            Constructor<D> constructor = (Constructor<D>) this.getClass().getDeclaredConstructor(clazz);
            for (E entity : entities) {
                dtos.add(constructor.newInstance(entity));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dtos;
    }

    @SuppressWarnings("unchecked")
    public <D extends AbstractDTO<E, ID>> Set<D> toDTOs(Class<E> clazz, Set<E> entities) {
        return (Set<D>) toDTOs(clazz, new LinkedHashSet<>(0), entities);

    }

    @SuppressWarnings("unchecked")
    public <D extends AbstractDTO<E, ID>> List<D> toDTOs(Class<E> clazz, List<E> entities) {
        return (List<D>) toDTOs(clazz, new ArrayList<>(0), entities);

    }

	public ID getId() {
		return id;
	}

	public void setId(ID id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
    
    
}
