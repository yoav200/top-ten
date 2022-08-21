package com.alhalel.topten.controllers;

import com.alhalel.topten.player.Player;
import com.alhalel.topten.player.PlayersService;
import com.alhalel.topten.player.model.PlayerData;
import com.alhalel.topten.player.model.PlayerItem;
import com.alhalel.topten.ranking.RankingService;
import com.alhalel.topten.ranking.RankingStatisticsService;
import com.alhalel.topten.security.UserPrincipal;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("players")
@AllArgsConstructor
public class PlayersController {

    private final PlayersService playersService;

    private final RankingService rankingService;

    private final RankingStatisticsService statisticsService;


    @GetMapping(value = {"", "/"})
    public String players(Model model) {
        return "players";
    }

    @GetMapping("/{playerId}")
    public String showPlayer(
            @PathVariable("playerId") String playerId,
            Model model,
            Principal p) {

        Player player = playersService.getPlayer(playerId);

        // add user ranking of player if exist
        Optional.ofNullable(p).ifPresent(pp -> {
            UserPrincipal principal = principalToUserPrincipal(p);
            rankingService.findRankingItem(principal.getId(), player.getId())
                    .ifPresent(item -> model.addAttribute("rankingItem", item));
        });
        // add player ranking statistics
        statisticsService.getPlayerStatistics(player.getUniqueName())
                .ifPresent(stats -> model.addAttribute("statistics", stats));

        model.addAttribute("player", player);
        model.addAttribute("playerData", playersService.getPlayerData(player));
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
                        .contains(query.toLowerCase()))
                .limit(15)
                .collect(Collectors.toList());
    }


    @GetMapping("/random")
    @ResponseBody
    public List<PlayerData> getRandomPlayers(@Value("${service.players.random-fetch-size}") int number) {
        return playersService.getRandomPlayers(number);
    }

    static UserPrincipal principalToUserPrincipal(Principal p) {
        return (UserPrincipal) ((OAuth2AuthenticationToken) p).getPrincipal();
    }

}
