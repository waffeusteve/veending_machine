package org.pkfrc.core.persistence.comment;

public abstract class CommentModel {

	protected String table;
	protected String column;
	protected String comment;

	public CommentModel() {
	}

	public CommentModel(String table, String comment) {
		this();
		this.table = table;
		this.comment = comment;
	}

	public CommentModel(String table, String column, String comment) {
		this(table, comment);
		this.column = column;
	}

	public abstract String exists();

	public abstract String create();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((column == null) ? 0 : column.hashCode());
		result = prime * result + ((table == null) ? 0 : table.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommentModel other = (CommentModel) obj;
		if (column == null) {
			if (other.column != null)
				return false;
		} else if (!column.equals(other.column))
			return false;
		if (table == null) {
			if (other.table != null)
				return false;
		} else if (!table.equals(other.table))
			return false;
		return true;
	}

}
