package com.alhalel.topten.tasks;

import com.alhalel.topten.player.PlayersService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@AllArgsConstructor
public class LoadPlayersTask {

    private final PlayersService playersService;

    @Scheduled(initialDelay = 5000, fixedDelay = 10000)
    @SchedulerLock(name = "scheduledTaskName", lockAtMostFor = "8m", lockAtLeastFor = "8m")
    public void scheduledTask() {
        log.info("LoadPlayersTask");
        // To assert that the lock is held (prevents misconfiguration errors)
        LockAssert.assertLocked();
        // do something
    }

}
