package com.alhalel.topten.ranking;

import com.alhalel.topten.ranking.RankList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RankListRepository extends JpaRepository<RankList, Long> {

    Optional<RankList> findByUserId(Long userId);
}
