package com.alhalel.topten.player;

import com.alhalel.topten.config.ScraperConfig;
import com.alhalel.topten.player.model.PlayerData;
import com.alhalel.topten.player.model.PlayerItem;
import com.alhalel.topten.scrapers.Scraper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Log4j2
@Service
@AllArgsConstructor
public class PlayersService {

    private static final Random random = new Random();

    private final Map<String, PlayerItem> playersItems = new ConcurrentHashMap<>();

    private final ScraperConfig config;

    private final List<Scraper> scrapers;

    private final PlayerRepository playerRepository;

    @PostConstruct
    public void init() {

        List<PlayerItem> mainScraperPlayers = getMainScraper().loadPlayers();

        Map<String, PlayerItem> collect = getSecondaryScraper()
                .map(scraper -> scraper.loadPlayers().stream().collect(Collectors.groupingBy(PlayerItem::getFullName)))
                .map(map -> mainScraperPlayers.stream()
                        .map(playerItem ->
                                Optional.ofNullable(map.get(playerItem.getFullName()))
                                        .map(list -> {
                                            if (list.size() == 1) {
                                                return PlayerItem.builder()
                                                        .uniqueName(playerItem.getUniqueName())
                                                        .playerId(list.get(0).getPlayerId())
                                                        .fullName(playerItem.getFullName())
                                                        .yearsActive(playerItem.getYearsActive())
                                                        .active(playerItem.isActive())
                                                        .build();
                                            } else {
                                                log.info("there are {} players with name {}. cannot match...",
                                                        list.size(),
                                                        playerItem.getFullName());
                                                return playerItem;
                                            }
                                        }).orElse(playerItem)
                        )).orElse(mainScraperPlayers.stream())
                .collect(Collectors.toMap(PlayerItem::getUniqueName, Function.identity()));

        playersItems.putAll(collect);
    }

    public Collection<PlayerItem> loadPlayersItems() {
        return playersItems.values();
    }

    public Player getPlayer(String uniqueName) {
        // validate uniqueName exist
        PlayerItem item = Optional.ofNullable(playersItems.get(uniqueName))
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        // get from DB or scrap
        Player player = getOrScrapForPlayer(item);

        // save if Eligible For Saving
        if (player.isEligibleForRanking()) {
            return playerRepository.save(player);
        }
        return player;
    }

    public Player getOrScrapForPlayer(PlayerItem playerItem) {
        return playerRepository.findPlayerByUniqueName(playerItem.getUniqueName()).map(player -> {
            try {
                return shouldUpdateInfo(player) ? updatePlayer(player, playerItem) : player;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).orElseGet(() -> {
            try {
                return getMainScraper().getPlayer(playerItem);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public List<PlayerData> getRandomPlayers(int number) {

        List<String> playersUniqueNames = playerRepository.getPlayersUniqueNames();

        List<String> players = new ArrayList<>();

        while (players.size() < number) {
            String uniqueName = playersUniqueNames.get(random.nextInt(playersUniqueNames.size()));
            players.add(uniqueName);
        }

        return playerRepository.findByUniqueNameIn(players).stream()
                .map(this::getPlayerData)
                .collect(Collectors.toList());
    }

    public PlayerData getPlayerData(Player player) {
        PlayerData playerData = new PlayerData(player);
        playerData.setExternalLink(getMainScraper().getPlayerUrl(player.getUniqueName()));

        getSecondaryScraper()
                .flatMap(s -> Optional.ofNullable(player.getPlayerReference()).map(integer -> s.getPlayerUrl(integer.toString())))
                .ifPresent(playerData::setExternalLink2);

        return playerData;
    }

    private boolean shouldUpdateInfo(Player player) {
        boolean missingInfo = StringUtils.isBlank(player.getPlayerInfo().getHeight())
                || StringUtils.isBlank(player.getPlayerInfo().getWeight())
                || StringUtils.isBlank(player.getPlayerInfo().getPosition());

        LocalDateTime updateDateTime = Optional.ofNullable(player.getUpdateDateTime()).orElse(LocalDateTime.now());

        long days = ChronoUnit.DAYS.between(updateDateTime, LocalDateTime.now());

        boolean updateActivePlayer = player.getPlayerInfo().isActive() && days > config.getActivePlayerUpdateDays();

        boolean updateInActivePlayer = !player.getPlayerInfo().isActive() && days > config.getInactivePlayerUpdateDays();

        return missingInfo || updateActivePlayer || updateInActivePlayer;
    }

    private Player updatePlayer(Player player, PlayerItem playerItem) throws IOException {
        player.update(getMainScraper().getPlayer(playerItem));
        return player;
    }

    private Scraper getMainScraper() {
        return scrapers.stream().filter(Scraper::isMain).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("must have main scraper"));
    }

    private Optional<Scraper> getSecondaryScraper() {
        return scrapers.stream().filter(s -> !s.isMain()).findFirst();
    }
}
