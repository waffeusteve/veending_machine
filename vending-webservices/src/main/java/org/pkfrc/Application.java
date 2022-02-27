package org.pkfrc;

import org.pkfrc.core.utilities.annontations.EntityScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <h1>Application</h1>
 * Application Entry point
 * <p>
 * PKFIE Research Center
 *
 *
 *
 * @author  Ulrich LELE - lele.ulrich2@gmail.com
 * @version 1.0
 * @since   03-02-2021
 */
@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan(basePackages = {"org.pkfrc"})
@EntityScan(basePackages = {"org.pkfrc"})
@EnableAsync
@EnableSwagger2
public class Application extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
