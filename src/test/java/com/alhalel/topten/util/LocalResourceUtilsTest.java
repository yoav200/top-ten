package com.alhalel.topten.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

@Disabled("Figure out class loader")
class LocalResourceUtilsTest {

    private LocalResourceUtils localResourceUtils;

    @BeforeEach
    void setUp() {
        localResourceUtils = new LocalResourceUtils();
        localResourceUtils.init();
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
    void getRandomBackgroundFileTest() throws FileNotFoundException {
        File file = localResourceUtils.getRandomBackgroundFile();
        Assertions.assertNotNull(file);
    }
}