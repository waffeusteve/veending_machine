package org.pkfrc.core.services.security;

import org.pkfrc.core.entities.security.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * To implement if user wants to redefine de default implementation of getAuthorities(User user)
 */

public interface GrantedAuthorityBuilder {

    Collection<? extends GrantedAuthority> getAuthorities(User user);
}
