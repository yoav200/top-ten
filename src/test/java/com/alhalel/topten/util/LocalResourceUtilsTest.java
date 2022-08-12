package com.alhalel.topten.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;

class LocalResourceUtilsTest {

    private LocalResourceUtils localResourceUtils;

    @BeforeEach
    void setUp() {
        localResourceUtils = new LocalResourceUtils();
    }

    @Test
    void defaultPlayerAvatarTest() {
        String s = LocalResourceUtils.defaultPlayerAvatar();
        Assertions.assertEquals("/images/no-profile-image.png", s);
    }

    @Test
    void getRandomBackgroundTest() {
        String randomBackground = localResourceUtils.getRandomBackground();
        Assertions.assertNotNull(randomBackground);
    }

    @Test
    void getRandomBackgroundFileTest() throws IOException {
        URL randomBackgroundFile = localResourceUtils.getRandomBackgroundFile();
        Assertions.assertNotNull(randomBackgroundFile);
    }
}