package com.alhalel.topten.player;

import com.alhalel.topten.player.model.PlayerData;
import com.alhalel.topten.player.model.PlayerItem;
import com.alhalel.topten.scrapers.BasketballReferenceScarper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Service
@AllArgsConstructor
public class PlayersService {

    private static final Random random = new Random();

    private static final String COMMA_DELIMITER = ",";

    private final ResourceLoader resourceLoader;

    private final Map<String, PlayerItem> playersItems = new ConcurrentHashMap<>();

    private final BasketballReferenceScarper scarper;

    private final PlayerRepository playerRepository;

    @PostConstruct
    public void init() {

        Resource resource = resourceLoader.getResource("classpath:data/basketball-reference-nba-players.csv");

        try (BufferedReader br = new BufferedReader(new FileReader(resource.getFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                PlayerItem playerItem = PlayerItem.builder()
                        //.id(Integer.valueOf(values[8]))
                        .uniqueName(values[0])
                        .fullName(values[1])
                        .yearsActive(values[2])
                        .active(BooleanUtils.toBoolean(values[3]))
                        .build();
                playersItems.put(playerItem.getUniqueName(), playerItem);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
