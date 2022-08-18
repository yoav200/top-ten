package com.alhalel.topten.scrapers;

import com.alhalel.topten.config.ScraperConfig;
import com.alhalel.topten.util.LocalResourceUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A scrapper for: <a href="https://www.nba.com/players">NBA players</a>
 * based on data from the URL: <a href="https://stats.nba.com/stats/playerindex">NBA Stats</a>
 */

@Log4j2
@Service
@AllArgsConstructor
public class NbaStatsScrapper {

    private static final String PLAYER_URL = "player/%s/%s";

    private final ScraperConfig config;

    private final LocalResourceUtils localResourceUtils;

    private ObjectMapper objectMapper;

    public String getPlayerUrl(String playerId, String uniqueName) {
        return config.getBasketballReferenceUrl() + String.format(PLAYER_URL, playerId, uniqueName);
    }

    public List<NbaStatsPlayerData> loadPlayersDataFile() {
        List<NbaStatsPlayerData> nbaStatsPlayerData = new ArrayList<>();

        try (InputStream is = localResourceUtils.loadResourceFile(LocalResourceUtils.PLAYERS_DATA_FILE_2)) {
            Iterator<JsonNode> playersSet = objectMapper.readTree(is).findPath("rowSet").elements();

            while (playersSet.hasNext()) {
                List<JsonNode> list = new ArrayList<>();
                playersSet.next().elements().forEachRemaining(list::add);
                try {
                    Assert.isTrue(list.size() == 26, "Wrong number of player properties");
                    nbaStatsPlayerData.add(NbaStatsPlayerData.builder()
                            .personId(list.get(0).asInt())
                            .playerLastName(list.get(1).asText())
                            .playerFirstName(list.get(2).asText())
                            .playerSlug(list.get(3).asText())
                            .teamId(list.get(4).asInt())
                            .teamSlug(list.get(5).asInt())
                            .isDefunct(list.get(6).asText())
                            .teamCity(list.get(7).asText())
                            .teamName(list.get(8).asText())
                            .teamAbbreviation(list.get(9).asText())
                            .jerseyNumber(list.get(10).asInt())
                            .position(list.get(11).asText())
                            .height(list.get(12).asText())
                            .weight(list.get(13).asText())
                            .college(list.get(14).asText())
                            .country(list.get(15).asText())
                            .draftYear(list.get(16).asInt())
                            .draftRound(list.get(17).asInt())
                            .draftNumber(list.get(18).asInt())
                            .rosterStatus(list.get(19).asDouble())
                            .pts(list.get(20).asDouble())
                            .reb(list.get(21).asDouble())
                            .ast(list.get(22).asDouble())
                            .statsTimeframe(list.get(23).asText())
                            .fromYear(list.get(24).asText())
                            .toYear(list.get(25).asText())
                            .build());
                } catch (Exception e) {
                    log.warn("Fail to parse player", e);
                }
            }
            return nbaStatsPlayerData;
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
