package org.pkfrc.core.services.security;


import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

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
