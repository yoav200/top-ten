package com.alhalel.topten.ranking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RankListRepository extends JpaRepository<RankList, Long> {

    Optional<RankList> findByUserId(Long userId);

    Optional<RankList> findByIdAndVisibilityNot(Long id, RankList.RankListVisibility visibility);

    Page<RankList> findByVisibilityNot(RankList.RankListVisibility visibility, Pageable pageable);
}
