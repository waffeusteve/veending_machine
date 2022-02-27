package org.pkfrc.core.persistence.vendors;

import java.text.MessageFormat;

import org.hibernate.dialect.PostgreSQL9Dialect;
import org.pkfrc.core.persistence.comment.PostgresSQLCommentModel;
import org.pkfrc.core.persistence.sequence.PostgresSQLSequenceModel;
import org.pkfrc.core.persistence.sequence.SequenceModel;
import org.springframework.orm.jpa.vendor.Database;

public class PostgresSQL implements Vendor {

	private static final String URL = "jdbc:postgresql://{0}:{1}/{2}";
	private static final String DRIVER_CLASS = "org.postgresql.Driver";
	private static final String HIBERNATE_DIALECT = "org.hibernate.dialect.PostgreSQLDialect";
	private static final String HIBERNATE_SPATIAL_DIALECT = "org.hibernate.spatial.dialect.postgis.PostgisDialect";

	private String host;
	private Long port;
	private String database;
	private PostgresSQLSequenceModel sequenceModel = new PostgresSQLSequenceModel();

	public PostgresSQL(String host, Long port, String database) {
		super();
		this.host = host;
		this.port = port;
		this.database = database;
	}

	@Override
	public String getPlatform() {
		return PostgreSQL9Dialect.class.getName();
	}

	@Override
	public Database getDatabase() {
		return Database.POSTGRESQL;
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
		if (spartial) {
			return HIBERNATE_SPATIAL_DIALECT;
		}
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
