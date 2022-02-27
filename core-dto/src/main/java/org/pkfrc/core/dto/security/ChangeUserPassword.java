package org.pkfrc.core.dto.security;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChangeUserPassword {

    private String login;
    private String current;
    private String password;
    private String passwordBis;

}
