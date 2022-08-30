package com.alhalel.topten.tasks;

import com.alhalel.topten.player.Player;
import com.alhalel.topten.player.PlayerRepository;
import com.alhalel.topten.player.PlayersService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.javacrumbs.shedlock.core.LockAssert;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Log4j2
//@Service
@AllArgsConstructor
public class LoadPlayersTask {

    private final PlayersService playersService;

    private final PlayerRepository playerRepository;

    private final Set<String> ignoredPlayers = Collections.synchronizedSet(new HashSet<>());


    //@Scheduled(initialDelay = 1000 * 60 * 2, fixedDelay = 1000 * 60 * 15)
    //@SchedulerLock(name = "scheduledTaskName", lockAtMostFor = "8m", lockAtLeastFor = "8m")
    public void scheduledTask() {
        long s = System.currentTimeMillis();
        log.info("LoadPlayersTask started at {}, players List size {}",
                LocalDateTime.ofInstant(Instant.ofEpochMilli(s), ZoneOffset.UTC),
                playersService.loadPlayersItems().size());

        // To assert that the lock is held (prevents misconfiguration errors)
        LockAssert.assertLocked();
        // do something

        playersService.loadPlayersItems().stream()
                .filter(playerItem -> !ignoredPlayers.contains(playerItem.getUniqueName()))
                .forEach(playerItem -> {
                    // get from DB or scrap
                    Player player = playersService.getOrScrapForPlayer(playerItem);

                    // save if Eligible For Saving
                    if (player.isEligibleForRanking()) {
                        playerRepository.save(player);
                    } else {
                        log.info("add to ignore list non-eligible player {}:{}",
                                playerItem.getFullName(),
                                playerItem.getUniqueName());
                        ignoredPlayers.add(playerItem.getUniqueName());
                    }
                });

        long e = System.currentTimeMillis();
        log.info("LoadPlayersTask finished at {}, elapse time {} seconds",
                LocalDateTime.ofInstant(Instant.ofEpochMilli(e), ZoneOffset.UTC),
                (e - s) / 1000);
    }
}
