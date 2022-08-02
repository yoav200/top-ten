package com.alhalel.topten.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "service.ranking")
public class RankingConfig {
    private long editorsChoice;
    private int minRanking;
    private int maxRanking;
}
