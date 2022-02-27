package org.pkfrc.core.persistence.vendors;

import java.text.MessageFormat;

import org.hibernate.dialect.Oracle10gDialect;
import org.pkfrc.core.persistence.comment.PostgresSQLCommentModel;
import org.pkfrc.core.persistence.sequence.OracleSequenceModel;
import org.pkfrc.core.persistence.sequence.SequenceModel;
import org.springframework.orm.jpa.vendor.Database;

public class Oracle implements Vendor {

	private static final String URL = "jdbc:oracle:thin:@{0}:{1}:{2}";
	private static final String DRIVER_CLASS = "oracle.jdbc.driver.OracleDriver";
	private static final String HIBERNATE_DIALECT = "org.hibernate.dialect.OracleDialect";
	private String host;
	private Long port;
	private String database;
	private OracleSequenceModel sequenceModel = new OracleSequenceModel();

	public Oracle(String host, Long port, String database) {
		super();
		this.host = host;
		this.port = port;
		this.database = database;
	}

	@Override
	public String getPlatform() {
		return Oracle10gDialect.class.getName();
	}

	@Override
	public Database getDatabase() {
		return Database.ORACLE;
	}

	@Override
	public String getDriverClass() {
		return DRIVER_CLASS;
	}

	@Override
	public String getUrl() {
		return MessageFormat.format(URL, host, String.valueOf(port), database);
	}

	@Override
	public String getDialect(boolean spartial) {
		return HIBERNATE_DIALECT;
	}

	@Override
	public SequenceModel getSequenceModel() {
		return sequenceModel;
	}

	@Override
	public Class<?> getCommentClazz() {
		return PostgresSQLCommentModel.class;
	}

}
