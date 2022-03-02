package org.pkfrc.projurise.ws.config;

import static java.util.Arrays.asList;

import org.pkfrc.core.ws.config.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${spring.h2.console.enabled:false}")
	private boolean h2ConsoleEnabled;

	@Value("${apidocsec.enabled:true}")
	private boolean apidocsecEnabled;

	@Bean
	public JwtTokenFilter jwtTokenFilter() {
		return new JwtTokenFilter();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Uncoment to activate
		http.csrf().disable().cors().and().exceptionHandling()
				.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
				.antMatchers(HttpMethod.GET, "/user/roles").hasAnyAuthority("ROLE_SELLER")
				.antMatchers((HttpMethod.POST), "/product/add").hasAnyAuthority("ROLE_SELLER")
				.antMatchers((HttpMethod.PUT), "/product/edit").hasAnyAuthority("ROLE_SELLER")
				.antMatchers((HttpMethod.DELETE), "/product/delete").hasAnyAuthority("ROLE_SELLER")
				.antMatchers((HttpMethod.POST), "/user/deposit").hasAnyAuthority("ROLE_BUYER")
				.antMatchers((HttpMethod.POST), "/purchase/buy").hasAnyAuthority("ROLE_BUYER")
				.antMatchers((HttpMethod.GET), "/user/reset").hasAnyAuthority("ROLE_BUYER")
				.antMatchers(HttpMethod.GET, "/").permitAll()
				.antMatchers(HttpMethod.POST, "/user/login").permitAll()
				.antMatchers(HttpMethod.POST, "/user/register").permitAll()
				.antMatchers(HttpMethod.GET, "/actuator/**")
				.permitAll().anyRequest().authenticated();

		http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(asList("*"));
		configuration.setAllowedMethods(asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
		configuration.setAllowCredentials(true);
		configuration.setAllowedHeaders(asList("Authorization", "Cache-Control", "Content-Type"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
