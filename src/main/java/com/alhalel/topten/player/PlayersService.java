package com.alhalel.topten.player;

import com.alhalel.topten.player.model.PlayerData;
import com.alhalel.topten.player.model.PlayerItem;
import com.alhalel.topten.scrapers.BasketballReferenceScarper;
import com.alhalel.topten.scrapers.NbaStatsPlayerData;
import com.alhalel.topten.scrapers.NbaStatsScrapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
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

    private final Map<Integer, NbaStatsPlayerData> nbaStatsPlayerData = new ConcurrentHashMap<>();

    private final BasketballReferenceScarper basketballReferenceScarper;

    private final NbaStatsScrapper nbaStatsScrapper;

    private final PlayerRepository playerRepository;

    @PostConstruct
    public void init() {

        Map<Integer, NbaStatsPlayerData> collect1 = nbaStatsScrapper.loadPlayersDataFile().stream()
                .collect(Collectors.toMap(NbaStatsPlayerData::getPersonId, Function.identity()));

        nbaStatsPlayerData.putAll(collect1);

        Map<String, PlayerItem> collect2 = basketballReferenceScarper.loadPlayersDataFile().stream()
                .collect(Collectors.toMap(PlayerItem::getUniqueName, Function.identity()));

        playersItems.putAll(collect2);
    }

    public Collection<PlayerItem> loadPlayersItems() {
        return playersItems.values();
    }

    public Collection<NbaStatsPlayerData> loadPlayersExtraInfo() {
        return nbaStatsPlayerData.values();
    }

    public Player getPlayer(String uniqueName) {
        // validate uniqueName exist
        PlayerItem item = Optional.ofNullable(playersItems.get(uniqueName))
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        // get from DB or scrap
        Player player = getOrScrapForPlayer(item);

        // save if Eligible For Saving
        if (player.isEligibleForSaving()) {
            return playerRepository.save(player);
        }
        return player;
    }

    public Player getOrScrapForPlayer(PlayerItem playerItem) {
        return playerRepository.findPlayerByUniqueName(playerItem.getUniqueName()).orElseGet(() -> {
            try {
                return basketballReferenceScarper.getPlayer(playerItem);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public List<PlayerData> getRandomPlayers(int number) {
        List<Player> all = playerRepository.findAll();
        List<PlayerData> players = new ArrayList<>();

        while (players.size() < number) {
            Player player = all.get(random.nextInt(all.size()));
            PlayerData playerData = getPlayerData(player);
            players.add(playerData);
        }
        return players;
    }

    public PlayerData getPlayerData(Player player) {
        PlayerData playerData = new PlayerData(player);
        playerData.setExternalLink(basketballReferenceScarper.getPlayerUrl(player.getUniqueName()));
        playerData.setExternalLink2(nbaStatsScrapper.getPlayerUrl(player.getPlayerReference()));
        return playerData;
    }

    public void updatePlayerInfo(Player player) {
        String playerFullName = player.getPlayerInfo().getFullName();
        List<NbaStatsPlayerData> collect = loadPlayersExtraInfo().stream().filter(data -> {
            String fullName = data.getPlayerFirstName() + " " + data.getPlayerLastName();
            return playerFullName.equalsIgnoreCase(fullName);
        }).collect(Collectors.toList());

        if (collect.size() == 1) {
            NbaStatsPlayerData data = collect.get(0);
            player.setPlayerReference(data.getPersonId());
            player.getPlayerInfo().setHeight(data.getHeight());
            player.getPlayerInfo().setWight(data.getWeight());
            player.getPlayerInfo().setPosition(data.getPosition());
            player.getPlayerInfo().setCountry(data.getCountry());
            player.getPlayerInfo().setNBADebut(data.getFromYear());
            player.getPlayerInfo().setDraft(Optional.ofNullable(data.getDraftYear()).map(Object::toString).orElse(""));
        } else if (collect.size() > 1) {
            log.info("NBA Stats contain multiple players by the name {}: [{}]", playerFullName, StringUtils.join(collect, ","));
        } else {
            log.info("NBA Stats has no player by the name {}", playerFullName);
        }
    }
}
