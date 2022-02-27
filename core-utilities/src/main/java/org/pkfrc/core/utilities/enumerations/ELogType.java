package org.pkfrc.core.utilities.enumerations;
public enum ELogType {

	Info(0), Warning(1), Error(2);
	private Integer logType;

	private ELogType(Integer logType) {
		this.logType = logType;
	}

	public Integer getLogType() {
		return logType;
	}

}