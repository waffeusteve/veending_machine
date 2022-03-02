package org.pkfrc.projurise.ws.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * <h1>SwaggerConfiguration</h1>
 * Swagger Configurations
 * <p>
 * PKFIE Research Center
 *
 *
 *
 * @author  Steve Waffeu - waffeusteve@gmail.com
 * @version 1.0
 * @since   03-02-2021
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

	/**
	 * // * Fonction retournant les elements qui seront utiles à swagger pour son
	 * affichage // * .apis pour filtrer sur quel critère sera affiché la
	 * documentation des webservices // * .paths pour filtrer sur le chemin des apis
	 * à afficher // * .apiInfo pour customiser les informations qui seront affichés
	 * sur la page web // * //
	 */
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class)).paths(PathSelectors.any())
				.build().apiInfo(getApiInfo());

	}

	/**
	 * Fonction permettant de construire les informations
	 *
	 */
	private ApiInfo getApiInfo() {
		ApiInfo apiInfo = new ApiInfoBuilder().title("Vending_Machine").description("The vending machine")
				.termsOfServiceUrl("Terms of service Url \nSupport: support@pkf-researchcenter.com")
				.license("Backend #1 Waffeu Steve License demo Version 1.0")
				.version("1.0").build();
		return apiInfo;
	}

}
