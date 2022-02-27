package org.pkfrc.core.persistence.vendors;

import java.text.MessageFormat;

import org.hibernate.dialect.MySQL5Dialect;
import org.pkfrc.core.persistence.comment.PostgresSQLCommentModel;
import org.pkfrc.core.persistence.sequence.MySQLSequenceModel;
import org.pkfrc.core.persistence.sequence.SequenceModel;
import org.springframework.orm.jpa.vendor.Database;

public class MySQL implements Vendor {

	private static final String URL = "jdbc:mysql://{0}:{1}/{2)";
	private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
	private static final String HIBERNATE_DIALECT = "org.hibernate.dialect.MySQLDialect";
	private String host;
	private Long port;
	private String database;
	private MySQLSequenceModel sequenceModel = new MySQLSequenceModel();

	public MySQL(String host, Long port, String database) {
		super();
		this.host = host;
		this.port = port;
		this.database = database;
	}

	@Override
	public String getPlatform() {
		return MySQL5Dialect.class.getName();
	}

	@Override
	public Database getDatabase() {
		return Database.MYSQL;
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
