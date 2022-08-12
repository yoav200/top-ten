package com.alhalel.topten.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

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
        byte[] randomBackgroundFile = localResourceUtils.getRandomBackgroundFile();
        Assertions.assertNotNull(randomBackgroundFile);
    }
}