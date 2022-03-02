package org.pkfrc.core.services.security;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LogOutResponse {
    Boolean success = false;
    String message;


    public LogOutResponse(String message){
        this.message = message;
    }
}
