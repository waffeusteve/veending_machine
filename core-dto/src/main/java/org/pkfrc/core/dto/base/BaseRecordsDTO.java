package org.pkfrc.core.dto.base;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = { "record" })
@SuppressWarnings("rawtypes")
public class BaseRecordsDTO<T extends Serializable> extends BaseRecordDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Set<T> records = new LinkedHashSet<T>(0);
	private long totalPages;
	private long totalElements;
	public Set<T> getRecords() {
		return records;
	}
	public void setRecords(Set<T> records) {
		this.records = records;
	}
	public long getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(long totalPages) {
		this.totalPages = totalPages;
	}
	public long getTotalElements() {
		return totalElements;
	}
	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}
	
	

}
