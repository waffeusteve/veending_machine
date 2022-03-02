package org.pkfrc.core.ws.config;

import org.pkfrc.core.entities.security.LocalJwt;
import org.pkfrc.core.entities.security.User;
import org.pkfrc.core.repo.security.ILocalJwtRepository;
import org.pkfrc.core.repo.security.UserRepository;
import org.pkfrc.core.services.security.DefaultGrantedAuthorityBuilder;
import org.pkfrc.core.services.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class JwtTokenFilter extends OncePerRequestFilter {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	ILocalJwtRepository jwtRepo;

	@Autowired
	private JwtService jwtService;

	@Autowired
	DefaultGrantedAuthorityBuilder authorityBuilder;

	private String header = "Authorization";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		getTokenString(request.getHeader(header)).ifPresent(token -> {
			jwtService.getSubFromToken(token).ifPresent(id -> {
				if (SecurityContextHolder.getContext().getAuthentication() == null) {
					User user = userRepository.getOne(Long.valueOf(id));
					if (user != null) {
						UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
								user, user.getUserName(), authorityBuilder.getAuthorities(user));
						List<LocalJwt> localJwt = jwtRepo.findByUserId(user.getId());
						if (localJwt.isEmpty()) {
							authenticationToken.setAuthenticated(false);
						}
						authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(authenticationToken);
					}
				}
			});
		});

		filterChain.doFilter(request, response);
	}

	private Optional<String> getTokenString(String header) {
		if (header == null) {
			return Optional.empty();
		} else {
			String[] split = header.split(" ");
			if (split.length < 2) {
				return Optional.empty();
			} else {
				return Optional.ofNullable(split[1]);
			}
		}
	}
}
