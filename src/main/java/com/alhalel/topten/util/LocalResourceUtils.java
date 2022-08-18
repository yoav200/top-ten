package com.alhalel.topten.util;

import com.google.common.io.ByteStreams;
import com.google.common.io.Resources;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Log4j2
@Component
public class LocalResourceUtils {

    public static final String PLAYERS_DATA_FILE_1 = "data/basketball-reference-nba-players.csv";

    public static final String PLAYERS_DATA_FILE_2 = "data/nba-stat-players.json";

    private static final String IMAGES_NO_PROFILE_IMAGE = "/images/player-icon.png";

    private static final String STATIC_IMAGES_BGS = "static/images/bgs/";

    private static final Random random = new Random();

    private final List<String> backgrounds = new ArrayList<>();

    public InputStream loadResourceFile(String path) throws IOException {
        return Resources.getResource(path).openStream();
    }

    public static String defaultPlayerAvatar() {
        return IMAGES_NO_PROFILE_IMAGE;
    }

    public String getRandomBackground() {
        List<String> list = getBackgrounds();
        return list.get(random.nextInt(list.size()));
    }

    public byte[] getRandomBackgroundFile() throws IOException {
        InputStream inputStream = Resources.getResource(STATIC_IMAGES_BGS + getRandomBackground()).openStream();
        return ByteStreams.toByteArray(inputStream);
    }

    private List<String> getBackgrounds() {
        if (backgrounds.isEmpty()) {
            backgrounds.addAll(loadBackgrounds(STATIC_IMAGES_BGS));
            loadBackgrounds("");
            loadBackgrounds("/");
            loadBackgrounds("static");
            loadBackgrounds("images/bgs");
        }
        return backgrounds;
    }

    private List<String> loadBackgrounds(String path) {
        log.info("loading resources from path {}", path);
        List<String> lines = new ArrayList<>();
        try {
            InputStream is = Resources.getResource(path).openStream();
            try (InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
                 BufferedReader reader = new BufferedReader(streamReader)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.info(line);
                    lines.add(line);
                }
            } finally {
                log.info("loaded {} lines", lines.size());
            }
        } catch (Exception e) {
            log.warn("Fail to load lines from path {}", path, e);
        }
        return lines;
    }
}
