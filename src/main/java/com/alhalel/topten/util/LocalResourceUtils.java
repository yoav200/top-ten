package com.alhalel.topten.util;

import com.google.common.io.Resources;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Log4j2
@Component
public class LocalResourceUtils {

    public static final String STATIC_IMAGES_BGS = "static/images/bgs/";

    private static final Random random = new Random();

    private final List<String> backgrounds = new ArrayList<>();


    public InputStream loadPlayersFile() throws IOException {
        return Resources.getResource("data/basketball-reference-nba-players.csv").openStream();
    }

    public static String defaultPlayerAvatar() {
        return "/images/no-profile-image.png";
    }

    public String getRandomBackground() {
        List<String> list = getBackgrounds();
        return list.get(random.nextInt(list.size()));
    }

    public URL getRandomBackgroundFile() {
        return Resources.getResource(STATIC_IMAGES_BGS + getRandomBackground());
    }

    private List<String> getBackgrounds() {
        if (backgrounds.isEmpty()) {
            loadBackgrounds();
        }
        return backgrounds;
    }

    private void loadBackgrounds() {
        try {
            URL resource = Resources.getResource(STATIC_IMAGES_BGS);

            File[] files = ResourceUtils.getFile(resource).listFiles();

            if (files != null) {
                for (File file : files) {
                    String path = file.getPath();
                    int indexOf = path.lastIndexOf("\\");
                    backgrounds.add(path.substring(indexOf + 1));
                }
            }
        } catch (FileNotFoundException e) {
            log.warn("Fail to load backgrounds", e);
        }
    }
}
