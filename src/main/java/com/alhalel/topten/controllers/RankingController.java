package com.alhalel.topten.controllers;

import com.alhalel.topten.enteties.Player;
import com.alhalel.topten.enteties.RankList;
import com.alhalel.topten.services.PlayersService;
import com.alhalel.topten.services.RankingService;
import com.alhalel.topten.services.RankingStatisticsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("ranking")
@AllArgsConstructor
public class RankingController {

    private final RankingService rankingService;

    private final PlayersService playersService;

    private final RankingStatisticsService statisticsService;

    @GetMapping("")
    String index(Model model) {
        rankingService.getRankingListForAccount().ifPresent(list -> {
            model.addAttribute("rankListStats", rankingService.getRankListStatistics(list));
            model.addAttribute("rankList", list);
        });
        return "ranking";
    }

    @PostMapping("/")
    public String updatePlayerRanking(
            Model model,
            @RequestParam(value = "playerId") String playerId,
            @RequestParam(value = "rank") Integer rank) {

        Player player = playersService.getPlayer(playerId);

        RankList rankList = rankingService.getRankingListForAccount()
                .orElseGet(rankingService::createRankingListForAccount);

        rankingService.updatePlayerRanking(rankList, player, rank);

        model.addAttribute("rankListStats", rankingService.getRankListStatistics(rankList));
        model.addAttribute("rankList", rankList);
        return "ranking";
    }


    @PostMapping("/remove")
    public String removePlayerRanking(Model model, @RequestParam(value = "playerId") String playerId) {

        Player player = playersService.getPlayer(playerId);

        rankingService.getRankingListForAccount().ifPresent(list -> {
            rankingService.removePlayerRanking(list, player);
        });

        statisticsService.getPlayerStatistics(player.getUniqueName())
                .ifPresent(stats -> model.addAttribute("statistics", stats));

        model.addAttribute("player", player);
        return "player";
    }

}
