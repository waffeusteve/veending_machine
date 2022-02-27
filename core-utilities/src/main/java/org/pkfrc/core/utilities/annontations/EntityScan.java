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

package org.pkfrc.core.utilities.annontations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author Gaetan
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface EntityScan {

	/**
	 * Alias for the {@link #basePackages()} attribute. Allows for more concise
	 * annotation declarations e.g.: {@code @EntityScan("org.smarttech.pkg")}
	 * instead of {@code @EntityScan(basePackages="org.smarttech.pkg")}.
	 */
	String[] value() default {};

	/**
	 * Base packages to scan for annotated components.
	 * <p>
	 * {@link #value()} is an alias for (and mutually exclusive with) this
	 * attribute.
	 */
	String[] basePackages() default {};

}
