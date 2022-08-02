package com.alhalel.topten.ranking;

import com.alhalel.topten.config.RankingConfig;
import com.alhalel.topten.player.Player;
import com.alhalel.topten.player.PlayerStats;
import com.alhalel.topten.ranking.model.RankListStatisticsModel;
import com.alhalel.topten.user.User;
import com.alhalel.topten.user.UserRepository;
import com.alhalel.topten.user.model.UserModel;
import com.alhalel.topten.util.ResourceUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@AllArgsConstructor
public class RankingService {

    private final RankListRepository rankListRepository;

    private final RankListItemRepository rankListItemRepository;

    private final UserRepository userRepository;

    private final RankingConfig rankingConfig;

    public Optional<RankList> getRankingListForUser(Long userId) {
        return rankListRepository.findByUserId(userId);
    }

    public Optional<RankListItem> findRankingItem(Long userId, Long playerId) {
        return rankListItemRepository.findByRankListUserIdAndPlayerId(userId, playerId);
    }

    public RankList updateRankingListVisibility(Long userId, RankList.RankListVisibility visibility) {
        return rankListRepository.findByUserId(userId).map(rankList -> {
            rankList.setVisibility(visibility);
            return rankListRepository.save(rankList);
        }).orElseThrow(() -> new IllegalArgumentException("Ranking list for user " + userId + " not found"));
    }

    public Page<RankList> findAll(Pageable paging) {
        User anonymous = new User();
        anonymous.setName("Anonymous");
        anonymous.setImageUrl(ResourceUtils.defaultPlayerAvatar());
        Page<RankList> page = rankListRepository.findByVisibilityNot(RankList.RankListVisibility.PRIVATE, paging);

        page.getContent().forEach(rankList -> {
            if (rankList.getVisibility().equals(RankList.RankListVisibility.SHARE_ANONYMOUSLY)) {
                rankList.setUser(anonymous);
            }
        });

        return page;
    }

    public Optional<RankList> findRankList(Long id) {
        return rankListRepository.findById(id);
    }

    public void updatePlayerRanking(
            RankList rankList,
            Player player,
            Integer rank) {

        if (!player.isEligibleForSaving()) {
            throw new IllegalArgumentException("Player " + player.getPlayerInfo().getFullName() + " is not Eligible for ranking");
        }

        if (rank < rankingConfig.getMinRanking() || rank > rankingConfig.getMaxRanking()) {
            throw new IllegalArgumentException("Rank must be between " + rankingConfig.getMinRanking() + " and " + rankingConfig.getMaxRanking());
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

    public UserModel getUserForList(RankList rankList) {
        return UserModel.builder()
                .name(rankList.getUser().getName())
                .imageUrl(rankList.getUser().getImageUrl())
                .build();
    }

    public RankList createRankingListForUser(Long userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    RankList rankList = new RankList();
                    rankList.setUser(user);
                    rankList.setTitle("Default list for user");
                    return rankListRepository.save(rankList);
                })
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
