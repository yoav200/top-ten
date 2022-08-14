package com.alhalel.topten.scrapers;

import com.alhalel.topten.config.ScraperConfig;
import com.alhalel.topten.util.LocalResourceUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class NbaStatsScrapperTest {

    private NbaStatsScrapper scrapper;

    @BeforeEach
    void setUp() {
        ScraperConfig config = new ScraperConfig();
        config.setBasketballReferenceUrl("https://www.nba.com/stats/");
        scrapper = new NbaStatsScrapper(config, new LocalResourceUtils(), new ObjectMapper());
    }

    @Test
    void getPlayerUrl() {
    }

    @Test
    void loadPlayersDataFile() {
        List<PlayerData> playerData = scrapper.loadPlayersDataFile();
        Assertions.assertNotNull(playerData);
        Assertions.assertEquals(4827, playerData.size());
    }
}