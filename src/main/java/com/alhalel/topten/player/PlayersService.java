package com.alhalel.topten.player;

import com.alhalel.topten.player.model.PlayerData;
import com.alhalel.topten.player.model.PlayerItem;
import com.alhalel.topten.scrapers.BasketballReferenceScarper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
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

    private final BasketballReferenceScarper scarper;

    private final PlayerRepository playerRepository;

    @PostConstruct
    public void init() {

        Map<String, PlayerItem> collect = scarper.loadPlayersDataFile().stream()
                .collect(Collectors.toMap(PlayerItem::getUniqueName, Function.identity()));

        playersItems.putAll(collect);
    }


    public Collection<PlayerItem> loadPlayersItems() {
        return playersItems.values();
    }

    public Player getPlayer(String uniqueName) {
        return Optional.ofNullable(playersItems.get(uniqueName))
                .map(playerItem -> playerRepository.findPlayerByUniqueName(uniqueName)
                        .orElseGet(() -> {
                            try {
                                Player player = scarper.getPlayer(playerItem);
                                // save only player with minimum of games
                                if (player.isEligibleForSaving()) {
                                    return playerRepository.save(player);
                                } else {
                                    return player;
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        })).orElseThrow(() -> new IllegalArgumentException("Player not found"));
    }

    public List<PlayerData> getRandomPlayers(int number) {

        int max = 8;
        int tries = 0;

        List<PlayerData> players = new ArrayList<>();
        List<String> keysAsArray = new ArrayList<>(playersItems.keySet());

        while (players.size() < number && tries < max) {
            String uniqueName = keysAsArray.get(random.nextInt(keysAsArray.size()));
            PlayerData playerData = getPlayerData(getPlayer(uniqueName));

            if (playerData.isEligibleForSaving()) {
                players.add(playerData);
            } else {
                playersItems.remove(uniqueName);
                keysAsArray.remove(uniqueName);
            }

            tries++;
        }
        return players;
    }

    public PlayerData getPlayerData(Player player) {
        PlayerData playerData = new PlayerData(player);
        playerData.setExternalLink(scarper.getPlayerUrl(player.getUniqueName()));
        return playerData;
    }
}
