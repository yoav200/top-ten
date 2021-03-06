package com.alhalel.topten.scrapers;

import com.alhalel.topten.config.ScraperConfig;
import com.alhalel.topten.enteties.Player;
import com.alhalel.topten.model.PlayerItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class BasketballReferenceScarperTest {

    private BasketballReferenceScarper crawler;

    @BeforeEach
    void setUp() {
        ScraperConfig config = new ScraperConfig();
        config.setBasketballReferenceUrl("https://www.basketball-reference.com/");
        crawler = new BasketballReferenceScarper(config);
    }

    @Test
    void ScarperForMJ() throws IOException {
        PlayerItem build = PlayerItem.builder()
                .uniqueName("jordami01")
                .build();

        Player player = crawler.getPlayer(build);

        Assertions.assertNotNull(player);
    }

    @Test
    void ScarperForDrJ() throws IOException {
        PlayerItem build = PlayerItem.builder()
                .uniqueName("ervinju01")
                .build();

        Player player = crawler.getPlayer(build);

        Assertions.assertNotNull(player);
    }

    @Test
    void ScarperForJA() throws IOException {
        PlayerItem build = PlayerItem.builder()
                .uniqueName("moranja01")
                .build();

        Player player = crawler.getPlayer(build);

        Assertions.assertNotNull(player);
    }

}