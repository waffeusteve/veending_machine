package org.pkfrc.core.repo.config;

import org.pkfrc.core.entities.config.ServerConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ServerConfigRepository extends JpaRepository<ServerConfig, Long> {

    public static final String FTP_SERVER_CONFIG_CODE= "gs.ftp";
    public static final String MAIL_SERVER_CONFIG_CODE= "gs.mail";
    ServerConfig findByCode(String code);

    @Query("select  s from ServerConfig s where s.code='"+FTP_SERVER_CONFIG_CODE+"'")
    ServerConfig getFtpServerConfig();

    @Query("select  s from ServerConfig s where s.code='"+MAIL_SERVER_CONFIG_CODE+"'")
    ServerConfig getMailServerConfig();
}
