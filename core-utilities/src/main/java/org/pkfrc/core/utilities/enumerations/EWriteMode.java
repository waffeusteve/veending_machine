package org.pkfrc.core.utilities.enumerations;

public enum EWriteMode {
	ltr, rtl;

	@Override
	public String toString() {
		switch (this) {
		case ltr:
			return "EWriteMode.ltr";
		case rtl:
			return "EWriteMode.rtl";
		default:
			return "";
		}
	}
}
