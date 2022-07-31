package com.alhalel.topten.scrapers;

import com.alhalel.topten.config.ScraperConfig;
import com.alhalel.topten.enteties.Player;
import com.alhalel.topten.enteties.PlayerAchievements;
import com.alhalel.topten.enteties.PlayerInfo;
import com.alhalel.topten.enteties.PlayerStats;
import com.alhalel.topten.model.PlayerItem;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
@Service
@AllArgsConstructor
public class BasketballReferenceScarper {

    // match pattern like: 2x AS MVP, 7x All-NBA, 4x NBA Champ
    private final static Pattern pattern1 = Pattern.compile("(\\d+)(x) (.*)$");

    // match pattern like: 2021-22 All-NBA, 1983 NBA Champ, 1967-68 AST Champ
    private final static Pattern pattern2 = Pattern.compile("(\\d{4})(-\\d{2})? (.*)$");

    private static final String PLAYER_URL = "players/%s/%s.html";

    private final ScraperConfig config;


    public Player getPlayer(PlayerItem playerItem) throws IOException {

        String uniqueName = playerItem.getUniqueName();
        String letter = uniqueName.substring(0, 1);
        String playerurl = config.getBasketballReferenceUrl() + String.format(PLAYER_URL, letter, uniqueName);

        Document doc = Jsoup.connect(playerurl).get();

        Player player = new Player();
        player.setUniqueName(playerItem.getUniqueName());
        player.setPlayerInfo(getPlayerInfo(playerItem, doc, "meta"));
        player.setAchievements(getPlayerAchievements(doc, "bling"));
        player.setPlayerStats(Set.of(
                getPlayerStats(doc, "per_game", "advanced", PlayerStats.StatsFor.REGULAR_SEASON),
                getPlayerStats(doc, "playoffs_per_game", "playoffs_advanced", PlayerStats.StatsFor.PLAYOFFS)
        ));

        if (player.getCareerPerGame().getGames() < config.getMinGamesLimit())
            player.setEligibleForSaving(false);

        return player;
    }

    public PlayerInfo getPlayerInfo(PlayerItem playerItem, Document doc, String id) {
        PlayerInfo.PlayerInfoBuilder infoBuilder = PlayerInfo.builder()
                .fullName(playerItem.getFullName())
                //.uniqueName(playerItem.getUniqueName())
                .yearsActive(playerItem.getYearsActive())
                .active(playerItem.isActive());

        Optional.ofNullable(doc.getElementById(id)).ifPresent(info -> {
            // player image
            Optional.ofNullable(info.select("div.media-item img").first())
                    .ifPresent(element -> infoBuilder.imageUrl(element.attr("src")));

            // DOB
            Optional.ofNullable(info.getElementById("necro-birth"))
                    .ifPresent(element -> infoBuilder.DOB(element.attr("data-birth")));

            //Elements elements = info.select("p");
        });

        return infoBuilder.build();
    }

    private PlayerAchievements getPlayerAchievements(Document doc, String id) {
        PlayerAchievements.PlayerAchievementsBuilder builder = PlayerAchievements.builder();

        Optional.ofNullable(doc.getElementById(id)).ifPresent(ul -> {
            for (Element item : ul.select("li")) {
                String itemValue = item.text();
                String award = "";
                int times = 0;

                Matcher matcher1 = pattern1.matcher(itemValue);
                Matcher matcher2 = pattern2.matcher(itemValue);

                if (matcher1.find()) {
                    award = matcher1.group(3);
                    times = Integer.parseInt(matcher1.group(1));
                } else if (matcher2.find()) {
                    award = matcher2.group(3);
                    times = 1;
                }

                if ("NBA Champ".equalsIgnoreCase(award)) {
                    builder.championships(times);
                } else if ("ABA Champ".equalsIgnoreCase(award)) {
                    builder.championshipsAba(times);
                } else if ("Finals MVP".equalsIgnoreCase(award)) {
                    builder.finalsMvp(times);
                } else if ("MVP".equalsIgnoreCase(award)) {
                    builder.leagueMvp(times);
                } else if ("Scoring Champ".equalsIgnoreCase(award)) {
                    builder.scoringChamp(times);
                } else if ("TRB Champ".equalsIgnoreCase(award)) {
                    builder.reboundChamp(times);
                } else if ("AST Champ".equalsIgnoreCase(award)) {
                    builder.assistChamp(times);
                } else if ("STL Champ".equalsIgnoreCase(award)) {
                    builder.stealsChamp(times);
                } else if ("BLK Champ".equalsIgnoreCase(award)) {
                    builder.blocksChamp(times);
                } else if ("Def. POY".equalsIgnoreCase(award)) {
                    builder.defensivePlayerOfTheYear(times);
                } else if ("All-NBA".equalsIgnoreCase(award)) {
                    builder.allNba(times);
                } else if ("All-Defensive".equalsIgnoreCase(award)) {
                    builder.allDefensive(times);
                } else if ("All Star".equalsIgnoreCase(award)) {
                    builder.allStar(times);
                } else if ("AS MVP".equalsIgnoreCase(award)) {
                    builder.allStarMvp(times);
                } else if ("ROY".equalsIgnoreCase(award)) {
                    builder.rookieOfTheYear(times);
                } else if ("Most Improved".equalsIgnoreCase(award)) {
                    builder.mostImprove(times);
                } else {
                    log.info("award {} not supported", itemValue);
                }
            }
        });

        return builder.build();
    }


    /**
     * @param doc player page document
     * @param id  playoffs_per_game, per_game
     * @return PlayerStats if found
     */
    private PlayerStats getPlayerStats(Document doc, String id, String id2, PlayerStats.StatsFor statsFor) {
        PlayerStats.PlayerStatsBuilder statsBuilder = PlayerStats.builder().statsFor(statsFor);

        // per game
        Optional.ofNullable(doc.getElementById(id))
                .flatMap(table -> table.select("tfoot tr").stream()
                        .filter(tr -> tr.select("th").text().equalsIgnoreCase("Career"))
                        .findFirst()).ifPresent(tr -> {
                    doubleValue(tr.select("td[data-stat=g]")).ifPresent(statsBuilder::games); // games played
                    doubleValue(tr.select("td[data-stat=pts_per_g]")).ifPresent(statsBuilder::ptsPerGame); // point per game
                    doubleValue(tr.select("td[data-stat=trb_per_g]")).ifPresent(statsBuilder::trbPerGme); // rebounds per game
                    doubleValue(tr.select("td[data-stat=ast_per_g]")).ifPresent(statsBuilder::astPerGame); // assists per game
                    doubleValue(tr.select("td[data-stat=stl_per_g]")).ifPresent(statsBuilder::stlPerGame); // steels  per game
                    doubleValue(tr.select("td[data-stat=blk_per_g]")).ifPresent(statsBuilder::blkPerGame); // blocks per game
                    doubleValue(tr.select("td[data-stat=tov_per_g]")).ifPresent(statsBuilder::tovPerGame); // turnovers per game
                    doubleValue(tr.select("td[data-stat=fg_pct]")).ifPresent(statsBuilder::fgPct); // field goals pct
                    doubleValue(tr.select("td[data-stat=fg3_pct]")).ifPresent(statsBuilder::fg3Pct); // 3 point pct
                    doubleValue(tr.select("td[data-stat=ft_pct]")).ifPresent(statsBuilder::ftPct); // free throws pct
                    doubleValue(tr.select("td[data-stat=efg_pct]")).ifPresent(statsBuilder::efgPct); // effective field goal pct
                });

        // advanced
        Optional.ofNullable(doc.getElementById(id2))
                .flatMap(table -> table.select("tfoot tr").stream()
                        .filter(tr -> tr.select("th").text().equalsIgnoreCase("Career"))
                        .findFirst()).ifPresent(tr -> {
                    doubleValue(tr.select("td[data-stat=per]")).ifPresent(statsBuilder::per); // player efficiency rating
                    doubleValue(tr.select("td[data-stat=ws]")).ifPresent(statsBuilder::ws); // win shares
                    doubleValue(tr.select("td[data-stat=ws_per_48]")).ifPresent(statsBuilder::wsPer48); //win shares per 48 minutes
                    doubleValue(tr.select("td[data-stat=bpm]")).ifPresent(statsBuilder::bpm); // box plus minus

                });

        return statsBuilder.build();
    }

    private Optional<Double> doubleValue(Elements elements) {
        return Optional.ofNullable(elements.first()).map(element -> {
            try {
                return Double.parseDouble(element.text());
            } catch (Exception e) {
                log.warn("Fail to get double value form element {}", element.nodeName());
                return 0D;
            }
        });
    }

    private void downloadImage(String imageUrl, String filepath) throws IOException {
        //Open a URL Stream
        Connection.Response resultImageResponse = Jsoup
                .connect(imageUrl)
                //.cookies(cookies)
                .ignoreContentType(true)
                .execute();

        // output here
        FileOutputStream out = (new FileOutputStream(filepath));
        out.write(resultImageResponse.bodyAsBytes());  // resultImageResponse.body() is where the image's contents are.
        out.close();
    }
}
