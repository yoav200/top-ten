package com.alhalel.topten.services;

import com.alhalel.topten.enteties.Player;
import com.alhalel.topten.model.PlayerItem;
import com.alhalel.topten.repositories.PlayerRepository;
import com.alhalel.topten.scrapers.BasketballReferenceScarper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Log4j2
@Service
@AllArgsConstructor
public class PlayersService {

    private static final String COMMA_DELIMITER = ",";

    @Autowired
    private final ResourceLoader resourceLoader;

    private final Map<String, PlayerItem> playersItems = new HashMap<>();

    private final BasketballReferenceScarper scarper;

    private final PlayerRepository playerRepository;

    @PostConstruct
    public void init() {

        Resource resource = resourceLoader.getResource("classpath:data/basketball-referense-nba-players.csv");

        //List<PlayerItem> playerItems = new ArrayList<>();
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
                //playerItems.add(playerItem);
                playersItems.put(playerItem.getUniqueName(), playerItem);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //playersItems.putAll(playerItems.stream().collect(Collectors.toMap(PlayerItem::getUniqueName, Function.identity())));
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
                                return playerRepository.save(player);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        })).orElseThrow(() -> new IllegalArgumentException("Player not found"));
    }
}
