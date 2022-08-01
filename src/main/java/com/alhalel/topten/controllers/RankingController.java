package com.alhalel.topten.controllers;

import com.alhalel.topten.player.Player;
import com.alhalel.topten.player.PlayersService;
import com.alhalel.topten.ranking.RankList;
import com.alhalel.topten.ranking.RankingService;
import com.alhalel.topten.ranking.model.RankingStatisticsService;
import com.alhalel.topten.security.UserPrincipal;
import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

import static com.alhalel.topten.controllers.PlayersController.principalToUserPrincipal;

@Controller
@RequestMapping("ranking")
@AllArgsConstructor
public class RankingController {

    private final RankingService rankingService;

    private final PlayersService playersService;

    private final RankingStatisticsService statisticsService;

    @Secured("ROLE_USER")
    @GetMapping("/my-list")
    String myRanking(Principal p, Model model) {
        UserPrincipal principal = principalToUserPrincipal(p);
        rankingService.getRankingListForUser(principal.getId()).ifPresent(list -> {
            model.addAttribute("rankListStats", rankingService.getRankListStatistics(list));
            model.addAttribute("rankList", list);
        });
        return "my-ranking";
    }

    @Secured("ROLE_USER")
    @PostMapping("/my-list")
    String changeListVisibility(
            @RequestParam(value = "visibility") RankList.RankListVisibility visibility,
            Principal p, Model model) {

        UserPrincipal principal = principalToUserPrincipal(p);

        RankList rankList = rankingService.updateRankingListVisibility(principal.getId(), visibility);

        model.addAttribute("rankListStats", rankingService.getRankListStatistics(rankList));
        model.addAttribute("rankList", rankList);

        return "ranking";
    }

    @Secured("ROLE_USER")
    @GetMapping("")
    String rankings(Principal p, Model model) {
        UserPrincipal principal = principalToUserPrincipal(p);
        rankingService.getRankingListForUser(principal.getId()).ifPresent(list -> {
            model.addAttribute("rankListStats", rankingService.getRankListStatistics(list));
            model.addAttribute("rankList", list);
        });
        return "ranking";
    }

    @Secured("ROLE_USER")
    @PostMapping("/")
    public String updatePlayerRanking(
            @RequestParam(value = "playerId") String playerId,
            @RequestParam(value = "rank") Integer rank,
            Principal p,
            Model model) {

        UserPrincipal principal = principalToUserPrincipal(p);

        Player player = playersService.getPlayer(playerId);

        RankList rankList = rankingService.getRankingListForUser(principal.getId())
                .orElseGet(() -> rankingService.createRankingListForUser(principal.getId()));

        rankingService.updatePlayerRanking(rankList, player, rank);

        model.addAttribute("rankListStats", rankingService.getRankListStatistics(rankList));
        model.addAttribute("rankList", rankList);
        return "ranking";
    }


    @Secured("ROLE_USER")
    @PostMapping("/remove")
    public String removePlayerRanking(
            @RequestParam(value = "playerId") String playerId,
            Principal p,
            Model model) {

        UserPrincipal principal = principalToUserPrincipal(p);

        Player player = playersService.getPlayer(playerId);

        rankingService.getRankingListForUser(principal.getId())
                .ifPresent(list -> rankingService.removePlayerRanking(list, player));

        statisticsService.getPlayerStatistics(player.getUniqueName())
                .ifPresent(stats -> model.addAttribute("statistics", stats));

        model.addAttribute("player", player);
        return "player";
    }
}
