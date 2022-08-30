package com.alhalel.topten.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "service.scrapers")
public class ScraperConfig {
    private String basketballReferenceUrl;
    private String nbaStats;
    private int minGamesLimit;
    private double minPer;
    private int activePlayerUpdateDays;
    private int inactivePlayerUpdateDays;

    public boolean isEligibleForRanking(double games, double per) {
        return games > getMinGamesLimit() && per > getMinPer();
    }
}
