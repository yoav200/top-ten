package com.alhalel.topten.util;

import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class LocalResourceUtils {

    public static final String STATIC_IMAGES_BGS = "/static/images/bgs/";

    private static final Random random = new Random();

    private final List<String> backgrounds = new ArrayList<>();


    @PostConstruct
    public void init() {
        try {
            URL resource = getClass().getClassLoader().getResource(STATIC_IMAGES_BGS);

            if (resource == null) {
                throw new IllegalArgumentException("Missing background images files");
            }

            File[] files = ResourceUtils.getFile(resource).listFiles();

            if (files != null) {
                for (File file : files) {
                    String path = file.getPath();
                    int indexOf = path.lastIndexOf("\\");
                    backgrounds.add(path.substring(indexOf + 1));
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public InputStream loadPlayersFile() {
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("/data/basketball-reference-nba-players.csv");

        if (resourceAsStream == null) {
            throw new IllegalArgumentException("Missing players file");
        }

        return resourceAsStream;
    }

    public static String defaultPlayerAvatar() {
        return "/images/no-profile-image.png";
    }

    public String getRandomBackground() {
        return backgrounds.get(random.nextInt(backgrounds.size()));
    }

    public File getRandomBackgroundFile() throws FileNotFoundException {
        URL resource = getClass().getClassLoader().getResource(STATIC_IMAGES_BGS + getRandomBackground());
        if (resource == null) {
            throw new IllegalArgumentException("Missing Image file");
        }
        return ResourceUtils.getFile(resource);
    }
}
