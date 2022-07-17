package com.alhalel.topten.scrapers;

import com.alhalel.topten.config.ScraperConfig;
import com.alhalel.topten.enteties.*;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
@Service
@AllArgsConstructor
public class BasketballReferenceScarper {


//    private final static String awardPattern = "\\d{4}-\\d{2}$|\\d{4}$|\\d+x$";
//
//    // pattern is d{4}-\d{2}$ badge or d+x badges. example: "1967-68 AST Champ" or "4x AST Champ"
//    private static final Set<String> awardsBadgesNames = Set.of(
//            "NBA Champ",
//            "Finals MVP",
//            "MVP",
//            "Scoring Champ",
//            "TRB Champ",
//            "AST Champ",
//            "STL Champ",
//            "BLK Champ",
//            "Def. POY",
//            "All-NBA",
//            "All-Defensive",
//            "All Star",
//            "AS MVP",
//            "ROY",
//            "Most Improved"
//    );

    private final static Pattern pattern = Pattern.compile("(\\d+)(x)$");


    private static final String PLAYER_URL = "players/%s/%s.html";

    private final ScraperConfig config;


    public Player getPlayer(PlayerItem playerItem) throws IOException {

        String uniqueName = playerItem.getUniqueName();
        String letter = uniqueName.substring(0, 1);
        String playerurl = config.getBasketballReferenceUrl() + String.format(PLAYER_URL, letter, uniqueName);

        Document doc = Jsoup.connect(playerurl).get();

        return Player.builder()
                //.id()
                .playerInfo(getPlayerInfo(playerItem, doc, "meta"))
                .careerPerGame(getPlayerStats(doc, "per_game").orElse(null))
                .playoffsPerGame(getPlayerStats(doc, "playoffs_per_game").orElse(null))
                .careerAdvanced(getPlayerAdvancedStats(doc, "advanced").orElse(null))
                .playoffsAdvanced(getPlayerAdvancedStats(doc, "playoffs_advanced").orElse(null))
                .achievements(getPlayerAchievements(doc, "bling").orElse(null))
                .build();
    }

    public PlayerInfo getPlayerInfo(PlayerItem playerItem, Document doc, String id) {
        Element infoDiv = doc.getElementById(id);

        String image = infoDiv.select("div.media-item img").first().attr("src");
        //String name = infoDiv.select("div h1").first().text();
        String dob = infoDiv.getElementById("necro-birth").attr("data-birth");

        Elements elements = infoDiv.select("p");


        return PlayerInfo.builder()
                .fullName(playerItem.getFullName())
                .uniqueName(playerItem.getUniqueName())
                .DOB(dob)
                .imageUrl(image)
                //.position()
                //.height()
                //.wight()
                //.NBADebut()
                //.yearsActive()
                .build();
    }

    private Optional<PlayerAchievements> getPlayerAchievements(Document doc, String id) {
        PlayerAchievements.PlayerAchievementsBuilder builder = PlayerAchievements.builder();
        return Optional.ofNullable(doc.getElementById(id)).map(ul -> {

            Elements items = ul.select("li");
            for (Element item : items) {
                String itemValue = item.text();

                String[] badgeParts = itemValue.split(" ", 2);

                if (badgeParts.length == 2) {
                    int times = 1;
                    String award = badgeParts[1];

                    Matcher matcher = pattern.matcher(badgeParts[0]);
                    if (matcher.find()) {
                        times = Integer.parseInt(matcher.group(1));
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
                        log.info("award {} not supported", award);
                    }
                }
            }
            return builder.build();
        });
    }


    /**
     * @param doc player page document
     * @param id  playoffs_per_game, per_game
     * @return PlayerStats if found
     */
    private Optional<PlayerStats> getPlayerStats(Document doc, String id) {
        return Optional.ofNullable(doc.getElementById(id))
                .flatMap(table -> table.select("tfoot tr").stream()
                        .filter(tr -> tr.select("th").text().equalsIgnoreCase("Career"))
                        .findFirst()
                        .map(tr -> {
                            String g = tr.select("td[data-stat=g]").first().text(); // games played
                            String pts_per_g = tr.select("td[data-stat=pts_per_g]").first().text(); // point per game
                            String trb_per_g = tr.select("td[data-stat=trb_per_g]").first().text(); // rebounds per game
                            String ast_per_g = tr.select("td[data-stat=ast_per_g]").first().text(); // assists per game
                            String stl_per_g = tr.select("td[data-stat=stl_per_g]").first().text(); // steels  per game
                            String blk_per_g = tr.select("td[data-stat=blk_per_g]").first().text(); // blocks per game
                            String tov_per_g = tr.select("td[data-stat=tov_per_g]").first().text(); // turnovers per game

                            String fg_pct = tr.select("td[data-stat=fg_pct]").first().text(); // field goals pct
                            String fg3_pct = tr.select("td[data-stat=fg3_pct]").first().text(); // 3 point pct
                            String ft_pct = tr.select("td[data-stat=ft_pct]").first().text(); // free throws pct
                            String efg_pct = tr.select("td[data-stat=efg_pct]").first().text(); // effective field goal pct

                            return PlayerStats.builder()
                                    .games(Double.parseDouble(g))
                                    .ptsPerGame(Double.parseDouble(pts_per_g))
                                    .astPerGame(Double.parseDouble(ast_per_g))
                                    .trbPerGme(Double.parseDouble(trb_per_g))
                                    .blkPerGame(Double.parseDouble(blk_per_g))
                                    .stlPerGame(Double.parseDouble(stl_per_g))
                                    .tovPerGame(Double.parseDouble(tov_per_g))
                                    .fgPct(Double.parseDouble(fg_pct))
                                    .ftPct(Double.parseDouble(ft_pct))
                                    .fg3Pct(Double.parseDouble(fg3_pct))
                                    .efgPct(Double.parseDouble(efg_pct))
                                    .build();
                        }));
    }


    /**
     * @param doc player page document
     * @param id  advanced, playoffs_advanced
     * @return PlayerAdvancedStats if found
     */
    private Optional<PlayerAdvancedStats> getPlayerAdvancedStats(Document doc, String id) {
        return Optional.ofNullable(doc.getElementById(id))
                .flatMap(table -> table.select("tfoot tr").stream()
                        .filter(tr -> tr.select("th").text().equalsIgnoreCase("Career"))
                        .findFirst()
                        .map(tr -> {
                            String per = tr.select("td[data-stat=per]").first().text(); // player efficiency rating
                            String ws = tr.select("td[data-stat=ws]").first().text(); // win shares
                            String ws48 = tr.select("td[data-stat=ws_per_48]").first().text(); //win shares per 48 minutes
                            String bpm = tr.select("td[data-stat=bpm]").first().text(); // box plus minus

                            return PlayerAdvancedStats.builder()
                                    .per(Double.parseDouble(per))
                                    .ws(Double.parseDouble(ws))
                                    .wsPer48(Double.parseDouble(ws48))
                                    .bpm(Double.parseDouble(bpm))
                                    .build();
                        }));
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
