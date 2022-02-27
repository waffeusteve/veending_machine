package org.pkfrc.core.persistence.comment;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Table;

import org.pkfrc.core.persistence.vendors.Vendor;
import org.pkfrc.core.utilities.annontations.Comment;
import org.pkfrc.core.utilities.helper.ScannerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommentHelper {
	protected static final Logger logger = LoggerFactory.getLogger(CommentHelper.class);

	public static Set<CommentModel> getComments(Vendor vendor, String clazzName) {
		Set<CommentModel> comments = new HashSet<>(0);

		logger.debug("Lecture de la classe : " + clazzName);
		Class<?> clazz = ScannerHelper.readClassType(clazzName);
		logger.debug("Création du ClassModel");
		String tableName;
		Table table = clazz.getDeclaredAnnotation(Table.class);
		if (table != null) {
			tableName = table.name();
		} else {
			tableName = clazz.getSimpleName();
		}
		Comment comment = clazz.getDeclaredAnnotation(Comment.class);

		if (comment != null) {
			comments.add(getInstance(vendor, tableName, null, comment.value()));
		}
		comments.addAll(getFieldComments(vendor, clazz.getFields(), tableName));
		comments.addAll(getMethodComments(vendor, clazz.getMethods(), tableName));
		return comments;
	}

	private static Set<CommentModel> getFieldComments(Vendor vendor, Field[] hierarchyFields, String tableName) {
		logger.debug("Lecture des fields depuis les fields");
		Set<CommentModel> comments = new HashSet<>(0);
		for (Field field : hierarchyFields) {
			if (!ScannerHelper.isPersistentField(field)) {
				continue;
			}
			Comment comment = field.getDeclaredAnnotation(Comment.class);
			if (comment != null) {
				String columnName;
				Column column = field.getDeclaredAnnotation(Column.class);
				if (column != null) {
					columnName = column.name();
				} else {
					columnName = field.getName();
				}
				comments.add(getInstance(vendor, tableName, columnName, comment.value()));
			}
		}
		return comments;
	}

	private static Set<CommentModel> getMethodComments(Vendor vendor, Method[] methods, String tableName) {
		logger.debug("Lecture des fields depuis les méthodes");
		Set<CommentModel> comments = new HashSet<>(0);
		for (Method method : methods) {
			if (!ScannerHelper.isPersistentMethod(method)) {
				continue;
			}
			Comment comment = method.getDeclaredAnnotation(Comment.class);
			if (comment != null) {
				String columnName;
				Column column = method.getDeclaredAnnotation(Column.class);
				if (column != null) {
					columnName = column.name();
				} else {
					columnName = ScannerHelper.fieldNameFromMethod(method);
				}
				comments.add(getInstance(vendor, tableName, columnName, comment.value()));
			}
		}
		return comments;
	}

	@SuppressWarnings("rawtypes")
	private static CommentModel getInstance(Vendor vendor, String table, String column, String comment) {

		Constructor constructor = null;

		try {
			constructor = vendor.getCommentClazz().getDeclaredConstructor(String.class, String.class, String.class);
			return (CommentModel) constructor.newInstance(table, column, comment);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
