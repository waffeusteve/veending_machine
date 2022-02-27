package org.pkfrc.core.utilities.helper;

import java.io.Serializable;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class SpringBeanHelper implements Serializable {

	private static final long serialVersionUID = 1L;

	Logger logger = LoggerFactory.getLogger(SpringBeanHelper.class);

	@Autowired
	private ApplicationContext context;

	/**
	 * 
	 * @param componentClass
	 * @return
	 */
	public static String formatBeanName(String componentClass) {
		return StringHelper.firstLetterToLowerCase(componentClass);
	}

	public static String getBeanNameFromClass(String clazz) {
		return formatBeanName(ScannerHelper.readClassType(clazz).getSimpleName());
	}

	public  <T> T getBean(Class<T> interfaceType) {
		T bean = null;
		try {
			bean = context.getBean(interfaceType);
		} catch (Exception e) {
			//logger.error("Bean Name " + beanName + " Not Found in Application Context", e);
		}
		return bean;
	}

	public Object getBean(String beanName) {

		Object bean = null;

		try {
			bean = context.getBean(beanName);
		} catch (Exception e) {
			logger.error("Bean Name " + beanName + " Not Found in Application Context", e);

		}

		return bean;
	}

	public <T> T getBean(String beanName, Class<T> interfaceType) {

		T bean = null;

		try {
			bean = context.getBean(beanName, interfaceType);
		} catch (Exception e) {
			logger.error("Bean Name " + beanName + " Not Found in Application Context", e);

		}

		return bean;
	}
}