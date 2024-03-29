package com.alhalel.topten.ranking;

import com.alhalel.topten.player.model.PlayerStatisticsModel;
import com.alhalel.topten.ranking.RankListItemRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@AllArgsConstructor
public class RankingStatisticsService {
    private final RankListItemRepository rankListItemRepository;

    public Optional<PlayerStatisticsModel> getPlayerStatistics(String playerUniqueName) {
        return rankListItemRepository.getPlayerStats(playerUniqueName);
    }
}
