package com.alhalel.topten.repositories;

import com.alhalel.topten.enteties.RankListItem;
import com.alhalel.topten.model.PlayerStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RankListItemRepository extends JpaRepository<RankListItem, Long> {

//    @Query(value = "SELECT min(rank) FROM RankListItem WHERE player.uniqueName = ?1")
//    public BigDecimal minRankByPlayerUniqueName(String uniqueName);
//
//    @Query(value = "SELECT max(rank) FROM RankListItem WHERE player.uniqueName = ?1")
//    public BigDecimal maxRankByPlayerUniqueName(String uniqueName);
//
//    @Query(value = "SELECT avg(rank) FROM RankListItem WHERE player.uniqueName = ?1")
//    public BigDecimal avgRankByPlayerUniqueName(String uniqueName);

    @Query(value = "SELECT " +
            "new com.alhalel.topten.model.PlayerStatistics(min(rank), max(rank), avg(rank)) " +
            "FROM RankListItem " +
            "WHERE player.uniqueName = ?1")
    Optional<PlayerStatistics> getPlayerStats(String uniqueName);
}
