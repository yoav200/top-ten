package com.alhalel.topten.services;

import com.alhalel.topten.enteties.Player;
import com.alhalel.topten.model.PlayerData;
import com.alhalel.topten.model.PlayerItem;
import com.alhalel.topten.repositories.PlayerRepository;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Log4j2
@Service
@AllArgsConstructor
public class PlayersService {

    private static final Random random = new Random();

    private static final String COMMA_DELIMITER = ",";

    private final ResourceLoader resourceLoader;

    private final Map<String, PlayerItem> playersItems = new HashMap<>();

    private final BasketballReferenceScarper scarper;

    private final PlayerRepository playerRepository;

    @PostConstruct
    public void init() {

        Resource resource = resourceLoader.getResource("classpath:data/basketball-referense-nba-players.csv");

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
                .map(playerItem ->
                        playerRepository.findPlayerByUniqueName(uniqueName).orElseGet(() -> {
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

        List<String> keysAsArray = new ArrayList<>(playersItems.keySet());

        return IntStream.rangeClosed(1, number).mapToObj(i -> {
            String uniqueName = keysAsArray.get(random.nextInt(keysAsArray.size()));
            return new PlayerData(getPlayer(uniqueName));
        }).collect(Collectors.toList());
    }
}
