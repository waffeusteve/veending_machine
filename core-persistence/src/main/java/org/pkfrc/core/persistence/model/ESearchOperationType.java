package org.pkfrc.core.persistence.model;

public enum ESearchOperationType {
	NULL, NOT_NULL, LESS_THAN, LESS_THAN_EQUAL,
	EQUAL, GREATER_THAN_EQUAL, GREATER_THAN, 
	NOT_EQUAL, LIKE, NOT_LIKE, BETWEEN, 
	IN,  ALIAS, ORDER;
}
