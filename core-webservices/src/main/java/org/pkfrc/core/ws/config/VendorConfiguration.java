/*
package org.pkfrc.core.ws.config;

import org.pkfrc.core.persistence.vendors.Vendor;
import org.pkfrc.core.utilities.annontations.EntityScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = { "org.pkfrc" })
@EntityScan(basePackages = { "org.pkfrc" })
public class VendorConfiguration {

	@Value("${database.type}")
	String type;

	@Value("${database.host}")
	String host;

	@Value("${database.port}")
	Long port;

	@Value("${database.name}")
	String name;

	@Value("${spring.datasource.username}")
	String username;

	@Value("${spring.datasource.password}")
	String password;

	@Bean(name = "vendor")
	@Primary
	Vendor getVendor() {
		return Vendor.getVendor(type, host, port, name);
	}


}*/
