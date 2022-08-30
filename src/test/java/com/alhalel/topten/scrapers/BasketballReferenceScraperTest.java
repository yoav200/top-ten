package com.alhalel.topten.scrapers;

import com.alhalel.topten.config.ScraperConfig;
import com.alhalel.topten.player.Player;
import com.alhalel.topten.player.model.PlayerItem;
import com.alhalel.topten.util.LocalResourceUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class BasketballReferenceScraperTest {

    private BasketballReferenceScraper scarper;

    @BeforeEach
    void setUp() {
        ScraperConfig config = new ScraperConfig();
        config.setBasketballReferenceUrl("https://www.basketball-reference.com/");
        scarper = new BasketballReferenceScraper(config, new LocalResourceUtils());
    }

    @Test
    void scarperForMJ() throws IOException {
        PlayerItem build = PlayerItem.builder()
                .uniqueName("jordami01")
                .build();

        Player player = scarper.getPlayer(build);

        Assertions.assertNotNull(player);
    }

    @Test
    void scarperForDrJ() throws IOException {
        PlayerItem build = PlayerItem.builder()
                .uniqueName("ervinju01")
                .build();

        Player player = scarper.getPlayer(build);

        Assertions.assertNotNull(player);
    }

    @Test
    void scarperForJA() throws IOException {
        PlayerItem build = PlayerItem.builder()
                .uniqueName("moranja01")
                .build();

        Player player = scarper.getPlayer(build);

        Assertions.assertNotNull(player);
    }

}