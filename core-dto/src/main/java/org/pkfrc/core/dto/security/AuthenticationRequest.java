package org.pkfrc.core.dto.security;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class AuthenticationRequest implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
    private String password;
}
