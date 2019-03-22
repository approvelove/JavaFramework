package com.lj.mysystem.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

//自定义配置

@Data
@Configuration
@ConfigurationProperties("base-config")
@Validated
public class LJConfig {
    @NotNull
    private String userName;
    @NotNull
    private String userPassword;
    @NotNull
    private String sqlIp;
    @NotNull
    private String sqlPwd;

    private String entryDate;
}
