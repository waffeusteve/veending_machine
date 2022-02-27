package org.pkfrc.core.persistence.vendors;

import java.text.MessageFormat;

import org.hibernate.dialect.SQLServerDialect;
import org.pkfrc.core.persistence.comment.PostgresSQLCommentModel;
import org.pkfrc.core.persistence.sequence.SQLServerSequenceModel;
import org.pkfrc.core.persistence.sequence.SequenceModel;
import org.springframework.orm.jpa.vendor.Database;

public class SqlServer implements Vendor {

	private static final String URL = "jdbc:sqlserver://{0}:{1};databaseName={2)";
	private static final String DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static final String HIBERNATE_DIALECT = "org.hibernate.dialect.SQLServerDialect";

	private String host;
	private Long port;
	private String database;
	private SQLServerSequenceModel sequenceModel = new SQLServerSequenceModel();

	public SqlServer(String host, Long port, String database) {
		super();
		this.host = host;
		this.port = port;
		this.database = database;
	}

	@Override
	public String getPlatform() {
		return SQLServerDialect.class.getName();
	}

	@Override
	public Database getDatabase() {
		return Database.SQL_SERVER;
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
