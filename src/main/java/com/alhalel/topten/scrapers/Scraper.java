package com.alhalel.topten.scrapers;

import com.alhalel.topten.player.Player;
import com.alhalel.topten.player.model.PlayerItem;

import java.io.IOException;
import java.util.List;

public interface Scraper {

    boolean isMain();
    List<PlayerItem> loadPlayers();

    String getPlayerUrl(String identifier);

    Player getPlayer(PlayerItem playerItem) throws IOException;
}
