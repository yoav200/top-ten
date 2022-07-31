package com.alhalel.topten.controllers;

import com.alhalel.topten.enteties.Player;
import com.alhalel.topten.enteties.RankList;
import com.alhalel.topten.security.UserPrincipal;
import com.alhalel.topten.services.PlayersService;
import com.alhalel.topten.services.RankingService;
import com.alhalel.topten.services.RankingStatisticsService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("ranking")
@AllArgsConstructor
public class RankingController {

    private final RankingService rankingService;

    private final PlayersService playersService;

    private final RankingStatisticsService statisticsService;

    @Secured("ROLE_USER")
    @GetMapping("/my-list")
    String myRanking(UserPrincipal principal, Model model) {
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
            UserPrincipal principal, Model model) {

        RankList rankList = rankingService.updateRankingListVisibility(principal.getId(), visibility);

        model.addAttribute("rankListStats", rankingService.getRankListStatistics(rankList));
        model.addAttribute("rankList", rankList);

        return "ranking";
    }

    @Secured("ROLE_USER")
    @GetMapping("")
    String rankings(UserPrincipal principal, Model model) {
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
            UserPrincipal principal,
            Model model) {

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
            UserPrincipal principal,
            Model model) {

        Player player = playersService.getPlayer(playerId);

        rankingService.getRankingListForUser(principal.getId()).ifPresent(list -> {
            rankingService.removePlayerRanking(list, player);
        });

        statisticsService.getPlayerStatistics(player.getUniqueName())
                .ifPresent(stats -> model.addAttribute("statistics", stats));

        model.addAttribute("player", player);
        return "player";
    }


}
