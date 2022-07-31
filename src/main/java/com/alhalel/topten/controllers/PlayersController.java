package com.alhalel.topten.controllers;

import com.alhalel.topten.enteties.Player;
import com.alhalel.topten.model.PlayerData;
import com.alhalel.topten.model.PlayerItem;
import com.alhalel.topten.security.UserPrincipal;
import com.alhalel.topten.services.PlayersService;
import com.alhalel.topten.services.RankingService;
import com.alhalel.topten.services.RankingStatisticsService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
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


    @GetMapping("")
    public String players(Model model) {
        return "players";
    }

    @GetMapping("/{playerId}")
    public String showPlayer(@PathVariable("playerId") String playerId,
                             Model model,
                             UserPrincipal principal) {

        Player player = playersService.getPlayer(playerId);

        rankingService.findRankingItem(player.getId(), player.getId())
                .ifPresent(item -> model.addAttribute("rankingItem", item));

//        rankingService.getRankingListForUser(principal.getId())
//                .flatMap(list -> list.getRankListItems().stream()
//                        .filter(i -> i.getPlayer().equals(player))
//                        .findFirst())
//                .ifPresent(item -> model.addAttribute("rankingItem", item));

        statisticsService.getPlayerStatistics(player.getUniqueName())
                .ifPresent(stats -> model.addAttribute("statistics", stats));


        model.addAttribute("player", player);
        return "player";
    }

    // ~ ============  JSON Response

    @GetMapping("/search")
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


    @GetMapping("/random")
    @ResponseBody
    public List<PlayerData> getRandomPlayers(@Value("${service.players.random-fetch-size}") int number) {
        return playersService.getRandomPlayers(number);
    }
}
