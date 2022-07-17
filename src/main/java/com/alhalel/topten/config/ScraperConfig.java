package com.alhalel.topten.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "service.scrapers")
public class ScraperConfig {
    private String basketballReferenceUrl;
}
