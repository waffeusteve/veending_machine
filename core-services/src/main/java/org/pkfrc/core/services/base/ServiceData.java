package org.pkfrc.core.services.base;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import org.pkfrc.core.services.enums.EServiceDataType;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author Steve Waffeu
 */
@NoArgsConstructor
@Data
public class ServiceData<E> implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean valid;
	private EServiceDataType type;
	private String message;
	private E record;
	private Set<E> records = new LinkedHashSet<E>(0);
	private long totalPages;
	private long totalElements;
	private Set<Validation> validations = new LinkedHashSet<>(0);

	public <T> ServiceData<E> convert(ServiceData<T> from) {
		ServiceData<E> sData = new ServiceData<E>();
		sData.message = from.message;
		sData.valid = from.valid;
		sData.type = from.type;
		sData.totalPages = from.totalPages;
		sData.totalElements = from.totalElements;
		sData.validations.addAll(from.validations);
		return sData;
	}

  @Override
  public String toString() {
    return "ServiceData{" +
      "valid=" + valid +
      ", type=" + type +
      ", message='" + message + '\'' +
      ", validations=" + validations +
      '}';
  }
}