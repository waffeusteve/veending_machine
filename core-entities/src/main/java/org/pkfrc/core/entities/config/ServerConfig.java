package org.pkfrc.core.entities.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pkfrc.core.entities.base.AbstractParamEntity;
import org.pkfrc.core.entities.base.ParamEntity;
import org.pkfrc.core.entities.enums.EServerType;

import javax.persistence.*;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SD_SERVER_CONFIG")
@SequenceGenerator(name = "SD_SERVER_CONFIG_SEQ", sequenceName = "SD_SERVER_CONFIG_SEQ", allocationSize = 1, initialValue = 1)
public class ServerConfig extends AbstractParamEntity<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "SERVER_CONFIG_ID")
    @GeneratedValue(generator = "SD_SERVER_CONFIG_SEQ", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name="SERVER_TYPE")
    private EServerType serverType;

    @Column(name="HOST")
    private String host;

    @Column(name="PORT")
    private Integer port;

    @Column(name="USERNAME")
    private String username;

    @Column(name="PASSWORD")
    private String password;

    @Column(name="HOME_FOLDER")
    private String homeFolder;

    @Column(name="DRIVER")
    private String driver;


    public int compareTo(ServerConfig arg0) {
        return id.compareTo(arg0.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServerConfig)) return false;
        if (!super.equals(o)) return false;
        ServerConfig file = (ServerConfig) o;
        return Objects.equals(code, file.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), code);
    }


    public int compareTo(ParamEntity<Long> o) {
        return 0;
    }
}
