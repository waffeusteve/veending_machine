package org.pkfrc.core.persistence.sequence;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.SequenceGenerator;

import org.pkfrc.core.persistence.base.CoreDAO;
import org.pkfrc.core.persistence.vendors.Vendor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class SequenceHelper {

	private CoreDAO dao;

	private Vendor vendor;

	public SequenceHelper() {
	}

	public Long getNextFromSequence(String sequence) {
		if (!isSequenceExist(sequence)) {
			createSequence(sequence);
		}
		return readSequenceNextValue(sequence);
	}

	/**
	 * Permet de vérifier qu'une sequence existe
	 * 
	 * @param sequenceName
	 * @return
	 */
	public boolean isSequenceExist(String sequence) {
		BigDecimal result;
		try {
			result = dao.findOneByQuery(vendor.getSequenceModel().exists(sequence));
		} catch (Exception e) {
			result = new BigDecimal(1);
		}
		return result.longValue() > 0;
	}

	/**
	 * Permet de créer une sequence
	 * 
	 * @param sequenceName
	 * @return
	 */
	public void createSequence(String sequence) {

		try {
			dao.executeNativeUpdate(vendor.getSequenceModel().create(sequence));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createSequence(SequenceGenerator sequence) {
		try {
			dao.executeNativeUpdate(vendor.getSequenceModel().create(sequence));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permet de vérifier qu'une sequence existe
	 * 
	 * @param sequenceName
	 * @return
	 */
	public Long readSequenceNextValue(String sequence) {
		BigInteger result;
		try {
			result = dao.findOneByQuery(vendor.getSequenceModel().nextValue(sequence));
		} catch (Exception e) {
			e.printStackTrace();
			result = new BigInteger("1");
		}
		return result.longValue();
	}

	public void setParams(Vendor vendor, CoreDAO dao) {
		this.vendor = vendor;
		this.dao = dao;
	}
}
