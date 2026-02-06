package com.hibitbackendimproved.config;

import com.hibitbackendimproved.config.oauth.KakaoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(KakaoProperties.class)
public class PropertiesConfig {
}
