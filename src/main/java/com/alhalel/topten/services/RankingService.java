package com.alhalel.topten.services;

import com.alhalel.topten.enteties.Player;
import com.alhalel.topten.enteties.PlayerStats;
import com.alhalel.topten.enteties.RankList;
import com.alhalel.topten.enteties.RankListItem;
import com.alhalel.topten.model.RankListStatisticsModel;
import com.alhalel.topten.repositories.RankListItemRepository;
import com.alhalel.topten.repositories.RankListRepository;
import com.alhalel.topten.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@AllArgsConstructor
public class RankingService {

    private final RankListRepository rankListRepository;

    private final RankListItemRepository rankListItemRepository;

    private final UserRepository userRepository;

    public Optional<RankList> getRankingListForUser(Long userId) {
        return rankListRepository.findByUserId(userId);
    }

    public Optional<RankListItem> findRankingItem(Long userId, Long playerId) {
        return rankListItemRepository.findByRankListUserIdAndPlayerId(userId, playerId);
    }

    public RankList updateRankingListVisibility(Long userId, RankList.RankListVisibility visibility) {
        return rankListRepository.findByUserId(userId).map(rankList -> {
            //rankList.setVisibility(visibility);
            return rankListRepository.save(rankList);
        }).orElseThrow(() -> new IllegalArgumentException("Ranking list for user " + userId + " not found"));
    }

    public void updatePlayerRanking(
            RankList rankList,
            Player player,
            Integer rank) {

        if (!player.isEligibleForSaving()) {
            throw new IllegalArgumentException("Player " + player.getPlayerInfo().getFullName() + " is not Eligible for ranking");
        }

        rankList.getRankListItems().stream()
                .filter(item -> item.getPlayer().equals(player))
                .findFirst()
                .ifPresentOrElse(
                        item -> rankList.updateRankItem(item, rank),
                        () -> rankList.addRankItem(RankListItem.builder().rank(rank).player(player).build()));

        if (rankList.isInvalidList()) {
            throw new IllegalArgumentException("Invalid Block. Found duplicate numbers");
        }

        rankListRepository.save(rankList);

    }

    public void removePlayerRanking(RankList rankList, Player player) {
        rankList.getRankListItems().removeIf(rankListItem -> rankListItem.getPlayer().equals(player));

        if (rankList.isInvalidList()) {
            throw new IllegalArgumentException("Invalid Block. Found duplicate numbers");
        }

        rankListRepository.save(rankList);
    }

    public RankList createRankingListForUser(Long userId) {
        return userRepository.findById(userId)
                .map(user -> rankListRepository.save(RankList.builder().user(user).title("Default list for user").build()))
                .orElseThrow(() -> new IllegalArgumentException("User not found " + userId));
    }

    public RankListStatisticsModel getRankListStatistics(RankList rankList) {
        RankListStatisticsModel statisticsModel = new RankListStatisticsModel();

        rankList.getRankListItems().forEach(item -> {
            if (item.getPlayer().getAchievements().getTotalChamps() > statisticsModel.getChamps()) {
                statisticsModel.setChamps(item.getPlayer().getAchievements().getTotalChamps());
            }
            if (item.getPlayer().getAchievements().getFinalsMvp() > statisticsModel.getFinalsMvp()) {
                statisticsModel.setFinalsMvp(item.getPlayer().getAchievements().getFinalsMvp());
            }
            if (item.getPlayer().getAchievements().getLeagueMvp() > statisticsModel.getLeagueMvp()) {
                statisticsModel.setLeagueMvp(item.getPlayer().getAchievements().getLeagueMvp());
            }
            item.getPlayer().getPlayerStats().stream()
                    .filter(a -> a.getStatsFor().equals(PlayerStats.StatsFor.REGULAR_SEASON))
                    .forEach(playerStats -> {
                        if (playerStats.getPtsPerGame() > statisticsModel.getPtsPerGame()) {
                            statisticsModel.setPtsPerGame(playerStats.getPtsPerGame());
                        }
                        if (playerStats.getAstPerGame() > statisticsModel.getAstPerGame()) {
                            statisticsModel.setAstPerGame(playerStats.getAstPerGame());
                        }
                        if (playerStats.getTrbPerGme() > statisticsModel.getTrbPerGme()) {
                            statisticsModel.setTrbPerGme(playerStats.getTrbPerGme());
                        }
                        if (playerStats.getBlkPerGame() > statisticsModel.getBlkPerGame()) {
                            statisticsModel.setBlkPerGame(playerStats.getBlkPerGame());
                        }
                        if (playerStats.getStlPerGame() > statisticsModel.getStlPerGame()) {
                            statisticsModel.setStlPerGame(playerStats.getStlPerGame());
                        }
                        if (playerStats.getPer() > statisticsModel.getPer()) {
                            statisticsModel.setPer(playerStats.getPer());
                        }
                        if (playerStats.getBpm() > statisticsModel.getBpm()) {
                            statisticsModel.setBpm(playerStats.getBpm());
                        }
                        if (playerStats.getWs() > statisticsModel.getWs()) {
                            statisticsModel.setWs(playerStats.getWs());
                        }
                    });
        });

        return statisticsModel;
    }
}
