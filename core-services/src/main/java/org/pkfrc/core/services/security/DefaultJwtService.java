package org.pkfrc.core.services.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.pkfrc.core.entities.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class DefaultJwtService implements JwtService {
    private String secret;
    private int sessionTime;
    private boolean addAuthorities;

    @Autowired
    DefaultGrantedAuthorityBuilder grantedAuthorityBuilder;

    @Autowired
    public DefaultJwtService(@Value("${jwt.secret}") String secret,
                             @Value("${jwt.sessionTime}") int sessionTime,
                             @Value("${jwt.addAuthorities:true}") boolean addAuthorities) {
        this.secret = secret;
        this.sessionTime = sessionTime;
    }

    @Override
    public String toToken(User user) {
        return Jwts.builder()
                .setSubject("" + user.getId())
                .claim("authorities", addAuthorities ? grantedAuthorityBuilder.getAuthorities(user) : new ArrayList<String>())
                .setExpiration(expireTimeFromNow())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    @Override
    public Optional<String> getSubFromToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            Optional<String> subject = Optional.ofNullable(claimsJws.getBody().getSubject());
            return subject;
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Date expireTimeFromNow() {
        return new Date(System.currentTimeMillis() + sessionTime * 1000);
    }
}
