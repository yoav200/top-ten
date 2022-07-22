package com.alhalel.topten.services;

import com.alhalel.topten.enteties.Player;
import com.alhalel.topten.enteties.RankList;
import com.alhalel.topten.enteties.RankListItem;
import com.alhalel.topten.repositories.RankListRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@AllArgsConstructor
public class RankingService {

    private final RankListRepository rankListRepository;


    public Optional<RankList> getRankingListForAccount() {
        return rankListRepository.findAll().stream().findFirst();
    }

    public RankList updatePlayerRanking(RankList rankList, Player player, Integer rank) {
        rankList.getRankListItems().stream()
                .filter(item -> item.getPlayer().equals(player))
                .findFirst()
                .ifPresentOrElse(item -> {
                    item.setRank(rank);
                }, () -> {
                    RankListItem item = RankListItem.builder()
                            .rank(rank)
                            .player(player)
                            .build();

                    rankList.getRankListItems().add(item);
                });


        rankListRepository.save(rankList);
        return rankList;
    }

    public RankList removePlayerRanking(RankList rankList, Player player) {
        rankList.getRankListItems().removeIf(rankListItem -> rankListItem.getPlayer().equals(player));
        return rankListRepository.save(rankList);
    }

    public RankList createRankingListForAccount() {
        RankList rankList = RankList.builder().build();
        return rankListRepository.save(rankList);
    }
}
