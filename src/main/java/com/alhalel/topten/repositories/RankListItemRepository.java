package com.alhalel.topten.repositories;

import com.alhalel.topten.enteties.RankListItem;
import com.alhalel.topten.model.PlayerStatisticsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RankListItemRepository extends JpaRepository<RankListItem, Long> {

    @Query(value = "SELECT " +
            "new com.alhalel.topten.model.PlayerStatisticsModel(min(rank), max(rank), avg(rank)) " +
            "FROM RankListItem " +
            "WHERE player.uniqueName = ?1")
    Optional<PlayerStatisticsModel> getPlayerStats(String uniqueName);
}
