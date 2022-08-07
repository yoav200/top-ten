package com.alhalel.topten.util;

import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class LocalResourceUtils {

    private static final Random random = new Random();

    private final List<String> backgrounds = new ArrayList<>();


    @PostConstruct
    public void init() {
        try {
            File[] files = ResourceUtils.getFile("classpath:static/images/bgs").listFiles();

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

    public static String defaultPlayerAvatar() {
        return "/images/no-profile-image.png";
    }

    public String getRandomBackground() {
        return backgrounds.get(random.nextInt(backgrounds.size()));
    }

    public File getRandomBackgroundFile() throws FileNotFoundException {
        String randomBackground = getRandomBackground();
        return ResourceUtils.getFile("classpath:static/images/bgs/" + randomBackground);
    }
}
