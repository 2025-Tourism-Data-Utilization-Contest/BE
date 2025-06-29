package com.saerok.showing.api.global.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.jwt")
public class JwtProperties {

    private String secretKey;
    private long accessTokenExpiration;
    private long refreshTokenExpiration;
    private String cookieDomain;
}
