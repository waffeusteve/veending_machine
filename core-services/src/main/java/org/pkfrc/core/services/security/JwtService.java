package org.pkfrc.core.services.security;

import org.pkfrc.core.entities.security.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface JwtService {

    String toToken(User user);

    Optional<String> getSubFromToken(String token);
}
