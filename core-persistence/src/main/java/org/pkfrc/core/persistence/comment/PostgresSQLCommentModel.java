package org.pkfrc.core.persistence.comment;

import org.pkfrc.core.utilities.helper.StringHelper;

public class PostgresSQLCommentModel extends CommentModel {
	public PostgresSQLCommentModel(String table, String column, String comment) {
		super(table, column, comment);
	}

	@Override
	public String exists() {
		return null;
	}

	@Override
	public String create() {
		String strSQL = "COMMENT ON ";
		if (StringHelper.isNotEmpty(column)) {
			strSQL += "COLUMN " + table + "." + column;
		} else {
			strSQL += "TABLE " + table;
		}
		strSQL += " IS '" + comment.replace("'", "''").toUpperCase() + "'";
		return strSQL;
	}

}
