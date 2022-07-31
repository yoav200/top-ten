package com.alhalel.topten.repositories;

import com.alhalel.topten.enteties.RankList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RankListRepository extends JpaRepository<RankList, Long> {

    Optional<RankList> findByUserId(Long userId);
}
