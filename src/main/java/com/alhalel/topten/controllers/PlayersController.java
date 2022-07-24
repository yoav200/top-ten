package com.alhalel.topten.controllers;

import com.alhalel.topten.enteties.Player;
import com.alhalel.topten.model.PlayerItem;
import com.alhalel.topten.services.PlayersService;
import com.alhalel.topten.services.RankingService;
import com.alhalel.topten.services.RankingStatisticsService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("players")
@AllArgsConstructor
public class PlayersController {

    private final PlayersService playersService;

    private final RankingService rankingService;

    private final RankingStatisticsService statisticsService;

    @GetMapping
    @ResponseBody
    public List<PlayerItem> playerItems(@RequestParam(value = "q", required = false) String query) {
        if (StringUtils.isBlank(query)) {
            return playersService.loadPlayersItems().stream()
                    .limit(15)
                    .collect(Collectors.toList());
        }

        return playersService.loadPlayersItems().stream()
                .filter(p -> p.getFullName()
                        .toLowerCase()
                        .contains(query))
                .limit(15)
                .collect(Collectors.toList());
    }


    @GetMapping("/{playerId}")
    public String showPlayer(Model model, @PathVariable("playerId") String playerId) {
        Player player = playersService.getPlayer(playerId);

        rankingService.getRankingListForAccount()
                .flatMap(list -> list.getRankListItems().stream()
                        .filter(i -> i.getPlayer().equals(player))
                        .findFirst())
                .ifPresent(item -> model.addAttribute("rankingItem", item));

        statisticsService.getPlayerStatistics(player.getUniqueName())
                .ifPresent(stats -> model.addAttribute("statistics", stats));


        model.addAttribute("player", player);
        return "player";
    }
}
