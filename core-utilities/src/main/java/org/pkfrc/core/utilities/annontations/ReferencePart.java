package org.pkfrc.core.utilities.annontations;

import org.pkfrc.core.utilities.enumerations.EWriteMode;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Documented
public @interface ReferencePart {

	String code();

	String label();

	String field();

	Segment[] segments();

	int length() default 0;

	EWriteMode padMode() default EWriteMode.rtl;

	EWriteMode trimMode() default EWriteMode.rtl;

	char padValue() default '0';
}
