package org.pkfrc.core.dto.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;
/**
 * @author Ulrich lele
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(value = { "record" })
public class BaseRecordsDTO<T extends Serializable> extends BaseRecordDTO {

	private Set<T> records = new LinkedHashSet<T>(0);
	private long totalPages;
	private long totalElements;

}
