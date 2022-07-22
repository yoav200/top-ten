package com.alhalel.topten.controllers;

import com.alhalel.topten.enteties.Player;
import com.alhalel.topten.enteties.RankList;
import com.alhalel.topten.services.PlayersService;
import com.alhalel.topten.services.RankingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("ranking")
@AllArgsConstructor
public class RankingController {

    private final RankingService rankingService;

    private final PlayersService playersService;

    @GetMapping("")
    String index(Model model) {

        model.addAttribute("now", LocalDateTime.now());

        rankingService.getRankingListForAccount().ifPresent(list -> {
            model.addAttribute("rankList", list);
        });

        return "ranking";
    }

    @PostMapping("/{playerId}")
    public String updatePlayerRanking(
            Model model,
            @PathVariable("playerId") String playerId,
            @RequestParam(value = "ranking", required = true) Integer rank) {

        Player player = playersService.getPlayer(playerId);


        RankList rankList = rankingService.getRankingListForAccount()
                .orElseGet(rankingService::createRankingListForAccount);

        rankingService.updatePlayerRanking(rankList, player, rank);

        model.addAttribute("rankList", rankList);
        return "ranking";
    }


    @DeleteMapping("/{playerId}")
    public String removePlayerRanking(Model model, @PathVariable("playerId") String playerId) {

        Player player = playersService.getPlayer(playerId);

        rankingService.getRankingListForAccount().ifPresent(list -> {
            rankingService.removePlayerRanking(list, player);
            model.addAttribute("rankList", list);
        });


        return "ranking";
    }

}
