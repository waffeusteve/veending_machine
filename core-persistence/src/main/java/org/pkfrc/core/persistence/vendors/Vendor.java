package org.pkfrc.core.persistence.vendors;

import org.pkfrc.core.persistence.sequence.SequenceModel;
import org.springframework.orm.jpa.vendor.Database;

public interface Vendor {

	String getPlatform();

	Database getDatabase();

	String getDriverClass();

	String getDialect(boolean spartial);

	String getUrl();

	SequenceModel getSequenceModel();

	Class<?> getCommentClazz();

	public static Vendor getVendor(String vendorStr, String host, Long port, String database) {
		Vendor vendor;
		switch (vendorStr) {
		case "MySQL":
			vendor = new MySQL(host, port, database);
			break;
		case "SqlServer":
			vendor = new SqlServer(host, port, database);
			break;
		case "PostgresSQL":
			vendor = new PostgresSQL(host, port, database);
			break;
		case "Oracle":
		default:
			vendor = new Oracle(host, port, database);
			break;
		}
		return vendor;
	}
}
