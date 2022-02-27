/*
 ***********************************************************************
 *                Confidentiality Information:                         *                    
 *                                                                     *
 * This module is the confidential and proprietary information of      * 
 * Smart Tech. It is not to be copied, reproduced, or transmitted in   *
 * any form, by any means, in whole or in part, nor is it to be used   *
 * for any purpose other than that for which it is expressly provided  *
 * without the written permission of Smart Tech.                       *
 *                                                                     *
 ***********************************************************************
 *                                                                     * 
 * PROGRAM DESCRIPTION:                                                *  
 *                                                                     *
 *                                                                     * 
 ***********************************************************************
 *                                                                     *
 * CHANGE HISTORY:                                                     *
 *                                                                     * 
 * Date:        by:         Reason:                                    * 
 * 2016-04-30   Gaetan     Initial Version.                            *
 * *********************************************************************
 */

package org.pkfrc.core.utilities.helper;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.hibernate.StaleObjectStateException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.util.Assert;

//@Component
//@Scope("prototype")
public class ScannerHelper implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ScannerHelper.class);

	/**
	 * Permet de savoir si un field est persistent Un Field est dit persistent s'il
	 * ne porte pas l'annotation @Transient
	 * 
	 * @param field
	 * @return Retourne true si le field est persistent et false si non
	 */
	public static boolean isPersistentField(Field field) {
		Assert.notNull(field, "The field should not be null");
		return field.isAnnotationPresent(Transient.class);
	}

	/**
	 * Permet de savoir si une méthode représente un field persistent. Une méthode
	 * représente un field persistent si elle ne possède pas l'annotation @Transient
	 * ou si elle comment par (get|is|has) et possède un typde de retour
	 * 
	 * @param method
	 * @return Retourne true si la méthode est persistent et false si non
	 */
	public static boolean isPersistentMethod(Method method) {
		Assert.notNull(method, "The method should not be null");
		if (!method.isAnnotationPresent(Transient.class) && !method.getName().equals("getClass")
				&& !method.getName().equals("hashCode")
				&& (method.getName().startsWith("get") || method.getName().startsWith("is")
						|| method.getName().startsWith("has"))
				&& method.getReturnType() != null && !method.getReturnType().getName().equals("java.lang.Object")) {
			return true;
		}
		return false;
	}

	/**
	 * Permet de retourner le fieldName à partir du nom de méthode. La methode doit
	 * etre un accesseur (getter|[setter)
	 * 
	 * @param method
	 * @return
	 */
	public static String fieldNameFromMethod(Method method) {
		Assert.notNull(method, "The method should not be null");
		String name = method.getName();
		if (method.getReturnType() == null) {
			name = name.replace("set", "");
		} else {
			if (name.startsWith("has")) {
				name = name.replace("has", "");
			} else if (name.startsWith("is")) {
				name = name.replace("is", "");
			} else {
				name = name.replace("get", "");
			}
		}
		return StringHelper.firstLetterToLowerCase(name);
	}

	/**
	 * Permet de retourner le nom de l'entité défini comme générique dans une
	 * déclaration
	 * 
	 * @param genericName
	 * @return
	 */
	public String objectNameFromGenericType(String genericName) {
		Assert.notNull(genericName, "The parametrer genericName must not be null");
		return genericName.substring(genericName.indexOf("<") + 1, genericName.indexOf(">"));
	}

	/**
	 * 
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static Field readClassField(Class<?> clazz, String fieldName) {
		if (clazz == null) {
			return null;
		}
		try {

			return clazz.getDeclaredField(fieldName);
		} catch (Exception e) {
			try {
				return clazz.getField(fieldName);
			} catch (Exception eX) {
				return readClassField(clazz.getSuperclass(), fieldName);
				// eX.printStackTrace();
				// return null;
			}
		}
	}

	public static Object buildEnumValue(Class<?> parentClazz, String fieldName, String value) {
		return buildEnumValue(getMethodReturnType(parentClazz, fieldName), value);
	}

	public static Object buildEnumValue(Class<?> clazz, String value) {
		List<?> values = Arrays.asList(clazz.getEnumConstants());
		for (int i = 0; i < values.size(); i++) {
			if (ScannerHelper.executeMethod(values.get(i), "name", null, null).equals(value)) {
				return values.get(i);
			}
		}
		return null;
	}

	public static Class<?> getMethodReturnType(Class<?> clazz, String fieldName) {
		String property = null;
		if (fieldName.contains(".")) {
			String props[] = fieldName.split("\\.");
			property = props[0];
			fieldName = fieldName.substring(props[0].length() + 1);
		} else {
			property = fieldName;
			fieldName = null;
		}

		Method method = readClassMethod(clazz, formatGetter(readClassField(clazz, property)));

		if (method == null) {
			return null;
		}
		clazz = method.getReturnType();
		if (method.getReturnType().isInterface()) {
			Type type = method.getGenericReturnType();
			if (type instanceof ParameterizedType) {
				Type[] args = ((ParameterizedType) type).getActualTypeArguments();
				if (args != null && args.length == 1) {
					clazz = (Class<?>) args[0];
				}
			}
		}
		if (fieldName != null) {
			return getMethodReturnType(clazz, fieldName);
		} else {
			return method.getReturnType();
		}
	}

	/**
	 * Computes correct instance of a field value based on its class
	 * 
	 * @param parentClazz
	 * @param fieldName
	 * @param value
	 * @return
	 */
	public static Object buildFieldValue(Class<?> parentClazz, String fieldName, Object value, boolean isList) {

		if (isNull(value)) {
			return null;
		}
		Class<?> clazz = getMethodReturnType(parentClazz, fieldName);

		if (clazz == null) {
			return null;
		}

		if (!isList) {
			return getValueByClass(parentClazz, fieldName, clazz, value);
		} else {
			List<Object> elements = new ArrayList<>(0);
			for (Object element : ((List<?>) value).toArray()) {
				elements.add(getValueByClass(parentClazz, fieldName, clazz, element));
			}
			return elements;
		}

	}

	private static Object getValueByClass(Class<?> parentClazz, String fieldName, Class<?> clazz, Object value) {
		if (clazz == String.class) {
			return (String) value;
		}
		if (clazz == Date.class && !(value instanceof Date) && StringHelper.isNotEmpty(value.toString())) {
			String plane = value.toString();
			Object date = DateHelper.parse(plane, DateHelper.formatDateHeure);
			if (date == null) {
				date = DateHelper.parse(plane);
			}

			return date;
		}
		if (clazz == Long.class) {
			return Long.valueOf(value.toString());
		}
		if (clazz == Double.class) {
			return Double.valueOf(value.toString());
		}
		if (clazz == BigDecimal.class) {
			return BigDecimal.valueOf(Double.valueOf(value.toString()));
		}
		if (clazz == Integer.class) {
			return Integer.valueOf(value.toString());
		}

		if (clazz.isEnum() && value instanceof String) {
			return buildEnumValue(parentClazz, fieldName, value.toString());
		}
		return value;
	}

	/**
	 * 
	 * @param fieldName
	 * @return
	 */
	public static String buildSetter(String fieldName) {
		fieldName = StringHelper.firstLetterToUpperCase(fieldName);
		return "set" + fieldName;
	}

	public static String formatGetter(Field field) {

		if (field == null) {
			return null;
		}
		String fieldName = field.getName();
		String getter = fieldName.substring(0, 1).toUpperCase();

		if (field.getType() == boolean.class) {
			getter = "is" + getter + fieldName.substring(1);
		} else {
			getter = "get" + getter + fieldName.substring(1);

		}

		return getter;
	}

	/**
	 * 
	 * @param fieldName
	 * @return
	 */
	public static String buildGetter(String fieldName) {
		fieldName = StringHelper.firstLetterToUpperCase(fieldName);
		return "get" + fieldName;
	}

	/**
	 * @param clazz
	 * @param field
	 * @return
	 */
	public static Method readClassMethod(Class<?> clazz, Field field) {
		Assert.notNull(clazz, "The parameter clazz must not be null");
		Assert.notNull(field, "The parameter field must not be null");
		String methodName = buildGetter(field.getName());
		try {
			return readClassMethod(clazz, methodName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param clazz
	 * @param methodName
	 * @param parametersType
	 * @return
	 */
	public static Method readClassMethod(Class<?> clazz, String methodName, Class<?>... parametersType) {
		Assert.notNull(clazz, "The parameter clazz must not be null");
		Assert.notNull(methodName, "The parameter field must not be null");
		try {
			if (parametersType == null || parametersType.length == 0) {
				return clazz.getMethod(methodName);
			} else {
				return clazz.getMethod(methodName, parametersType);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Permet de lire la valeur d'un field sur une entité
	 * 
	 * @param record
	 * @param fieldName
	 * @return
	 */
	public static Object readFieldValue(Object record, String fieldName) {
		if (record == null) {
			return null;
		}
		String property = null;
		if (fieldName.contains(".")) {
			String props[] = fieldName.split("\\.");
			property = props[0];
			fieldName = fieldName.substring(props[0].length() + 1);
		} else {
			property = fieldName;
			fieldName = null;
		}

		Method method = readClassMethod(record.getClass(), buildGetter(property));
		if (method == null) {
			return null;
		}
		Object value;
		try {
			value = method.invoke(record);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to retrieve value for " + fieldName);
		}
		if (fieldName != null) {
			return readFieldValue(value, fieldName);
		} else {
			return value;
		}
	}

	/**
	 * 
	 * @param record
	 * @param fieldName
	 * @param value
	 */
	public static void setFieldValue(Object record, String fieldName, Object value) {

		if (record == null) {
			return;
		}

		String property = fieldName;
		if (fieldName.contains(".")) {
			String props[] = fieldName.split("\\.");
			fieldName = props[props.length - 1];
			property = property.substring(0, property.length() - (fieldName.length() + 1));
		} else {
			property = null;
		}
		if (property != null) {
			// Je dois modifier l'object record
			record = readFieldValue(record, property);
		}

		Field field = readClassField(record.getClass(), fieldName);

		if (field == null) {
			logger.error("Failed to find field " + fieldName + " in class heirachy : " + record.getClass());
			return;
		}

		try {
			executeMethod(record, buildSetter(field.getName()), new Class[] { field.getType() },
					new Object[] { value });

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to set value for " + fieldName);
		}
	}

	@SuppressWarnings("rawtypes")
	public static Object executeMethod(Object entity, String methodName, Class[] parameterTypes,
			Object[] parameterValues) {

		Object returnValue;
		try {

			Method method = readClassMethod(entity.getClass(), methodName, parameterTypes);
			if (method == null) {
				return null;
			}
			returnValue = method.invoke(entity, parameterValues);

		} catch (InvocationTargetException invkexcp) {

			logger.warn("Reflection Error :" + invkexcp.getCause().getMessage());
			logger.warn("Unable to Execute Method " + methodName + " For Object " + entity.getClass().getSimpleName());

			if (invkexcp.getTargetException().getCause().getClass().equals(StaleObjectStateException.class)) {
				throw new  OptimisticLockingFailureException(invkexcp.getCause().getMessage(),
						invkexcp.getCause());
			} else {
				throw new RuntimeException(
						"Unable to Execute Method " + methodName + " For Object " + entity.getClass().getSimpleName());
			}

		} catch (Exception e) {

			logger.warn("Reflection Error :" + e.getMessage());
			logger.warn("Unable to Execute Method " + methodName + " For Object " + entity.getClass().getSimpleName());

			throw new RuntimeException(
					"Unable to Execute Method " + methodName + " For Object " + entity.getClass().getSimpleName());

		}

		return returnValue;
	}

	/**
	 * read the class from its name
	 * 
	 * @param className
	 * @return
	 */
	public static Class<?> readClassType(String className) {
		Assert.notNull(className, "The className must not be null");
		try {
			return Class.forName(className);
		} catch (Exception e) {
			throw new RuntimeException("Unknow class name : " + className);
		}
	}

	public static boolean isNull(Object object) {
		if (object instanceof String) {
			return StringHelper.isEmpty((String) object);
		}
		return object == null;
	}
}
