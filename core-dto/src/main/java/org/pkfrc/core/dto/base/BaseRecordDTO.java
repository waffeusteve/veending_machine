package org.pkfrc.core.dto.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;
/**
 * @author Steve Waffeu
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BaseRecordDTO<T extends Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean valid;
	private String message;
	private T record;
	private Set<String> validations = new LinkedHashSet<>(0);
}
