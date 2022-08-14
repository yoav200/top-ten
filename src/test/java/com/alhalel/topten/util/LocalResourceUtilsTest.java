package com.alhalel.topten.util;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

class LocalResourceUtilsTest {

    private LocalResourceUtils localResourceUtils;

    @BeforeEach
    void setUp() {
        localResourceUtils = new LocalResourceUtils();
    }

    @Test
    void defaultPlayerAvatarTest() {
        String s = LocalResourceUtils.defaultPlayerAvatar();
        Assertions.assertEquals("/images/player-icon.png", s);
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

    @Test
    void breakNumberTest() {

        List<Integer> list = List.of(1234, 4578, 100, 603);

        Function<Integer, List<String>> breakNumber = (n) -> {
            List<String> nums = new ArrayList<>();
            while (n >= 10) {
                nums.add(Integer.toString(n % 10));
                n = n / 10;
            }
            nums.add(Integer.toString(n));
            return nums;
        };

        list.forEach(n -> System.out.println(n + " break into: " + StringUtils.join(breakNumber.apply(n), ",")));
    }
}