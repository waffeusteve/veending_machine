package org.pkfrc.core.services.security;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.pkfrc.core.entities.security.Fonctionalite;
import org.pkfrc.core.entities.security.Profile;

import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
public class AuthResponse {
    Boolean success = false;
    String message;
    Serializable user;


    public AuthResponse(String message){
        this.message = message;
    }
}
