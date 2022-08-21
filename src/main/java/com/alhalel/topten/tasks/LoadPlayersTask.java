package com.alhalel.topten.tasks;

import com.alhalel.topten.player.Player;
import com.alhalel.topten.player.PlayerRepository;
import com.alhalel.topten.player.PlayersService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Log4j2
@Service
@AllArgsConstructor
public class LoadPlayersTask {

    private static final int UPDATE_ACTIVE_PLAYER = 7;

    private static final int UPDATE_INACTIVE_PLAYER = 30;

    private final PlayersService playersService;

    private final PlayerRepository playerRepository;


    @Scheduled(initialDelay = 1000 * 60 * 2, fixedDelay = 1000 * 60 * 15)
    @SchedulerLock(name = "scheduledTaskName", lockAtMostFor = "8m", lockAtLeastFor = "8m")
    public void scheduledTask() {
        long s = System.currentTimeMillis();
        log.info("LoadPlayersTask started at {}, players List size {}",
                LocalDateTime.ofInstant(Instant.ofEpochMilli(s), ZoneOffset.UTC),
                playersService.loadPlayersItems().size());

        // To assert that the lock is held (prevents misconfiguration errors)
        LockAssert.assertLocked();
        // do something

        playersService.loadPlayersItems().forEach(playerItem -> {
            // get from DB or scrap
            Player player = playersService.getOrScrapForPlayer(playerItem);

            if (player.isEligibleForSaving() && shouldUpdateInfo(player)) {
                log.info("Updating player {}", playerItem.getFullName());
                playersService.updatePlayerInfo(player);
            }
            // save if Eligible For Saving
            if (player.isEligibleForSaving()) {
                playerRepository.save(player);
            } else {
                log.info("removing non-eligible player {}:{}", playerItem.getFullName(), playerItem.getUniqueName());
                playersService.removePlayer(playerItem);
            }
        });

        long e = System.currentTimeMillis();
        log.info("LoadPlayersTask finished at {}, elapse time {} seconds",
                LocalDateTime.ofInstant(Instant.ofEpochMilli(e), ZoneOffset.UTC),
                (e - s) / 1000);
    }


    private boolean shouldUpdateInfo(Player player) {
        boolean missingInfo = StringUtils.isBlank(player.getPlayerInfo().getHeight())
                || StringUtils.isBlank(player.getPlayerInfo().getWight())
                || StringUtils.isBlank(player.getPlayerInfo().getPosition())
                || StringUtils.isBlank(player.getPlayerInfo().getCountry());

        LocalDateTime updateDateTime = Optional.ofNullable(player.getUpdateDateTime()).orElse(LocalDateTime.now());

        long days = ChronoUnit.DAYS.between(updateDateTime, LocalDateTime.now());

        boolean updateActivePlayer = player.getPlayerInfo().isActive() && days > UPDATE_ACTIVE_PLAYER;

        boolean updateInActivePlayer = !player.getPlayerInfo().isActive() && days > UPDATE_INACTIVE_PLAYER;

        return missingInfo || updateActivePlayer || updateInActivePlayer;
    }
}
