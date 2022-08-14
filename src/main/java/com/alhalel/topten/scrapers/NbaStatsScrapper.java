package com.alhalel.topten.scrapers;

import com.alhalel.topten.config.ScraperConfig;
import com.alhalel.topten.util.LocalResourceUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * A scrapper for: <a href="https://www.nba.com/players">NBA players</a>
 * based on data from the URL: https://stats.nba.com/stats/playerindex
 */

@Log4j2
@Service
@AllArgsConstructor
public class NbaStatsScrapper {

    private static final String PLAYERS_DATA_FILE = "data/basketball-reference-nba-players.csv";

    private static final String PLAYER_URL = "player/%s/%s";


    private final ScraperConfig config;

    private final LocalResourceUtils localResourceUtils;


    public String getPlayerUrl(String playerId, String uniqueName) {
        return config.getBasketballReferenceUrl() + String.format(PLAYER_URL, playerId, uniqueName);
    }

}
