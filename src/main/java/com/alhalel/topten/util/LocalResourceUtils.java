package com.alhalel.topten.util;

import com.google.common.io.ByteStreams;
import com.google.common.io.Resources;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
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

    private final List<String> backgrounds = List.of(
            "alex-george-J7qnlH2PUpg-unsplash.jpg",
            "edgar-chaparro-kB5DnieBLtM-unsplash.jpg",
            "jason-leung-nM2WEy42Npg-unsplash.jpg",
            "tj-dragotta-Gl0jBJJTDWs-unsplash.jpg",
            "tom-briskey-AddAnDkkovM-unsplash.jpg",
            "WallpaperDog-10707280.jpg",
            "WallpaperDog-10881787.jpg",
            "WallpaperDog-10881795.jpg",
            "WallpaperDog-10881797.jpg",
            "WallpaperDog-10881820.jpg",
            "WallpaperDog-10881823.jpg",
            "WallpaperDog-10881829.jpg",
            "WallpaperDog-10881832.jpg",
            "WallpaperDog-10881867.jpg",
            "WallpaperDog-10881895.jpg",
            "WallpaperDog-10881996.jpg",
            "WallpaperDog-14636.jpg",
            "WallpaperDog-899041.jpg",
            "WallpaperDog-963777.jpg",
            "WallpaperDog-963786.jpg",
            "WallpaperDog-963787.jpg",
            "WallpaperDog-963800.jpg",
            "WallpaperDog-963817.jpg",
            "WallpaperDog-963900.jpg");


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
        return backgrounds;
    }

//    private List<String> loadBackgrounds(String path) {
//        log.info("loading resources from path {}", path);
//        List<String> lines = new ArrayList<>();
//        try {
//            InputStream is = Resources.getResource(path).openStream();
//            try (InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
//                 BufferedReader reader = new BufferedReader(streamReader)) {
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    log.info(line);
//                    lines.add(line);
//                }
//            } finally {
//                log.info("loaded {} lines", lines.size());
//            }
//        } catch (Exception e) {
//            log.warn("Fail to load lines from path {}", path, e);
//        }
//        return lines;
//    }
}
