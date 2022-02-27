package org.pkfrc.core.dto.security;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChangePassword {

    private Long id;
    private String password;
}
