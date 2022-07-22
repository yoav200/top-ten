package com.alhalel.topten.controllers;

import com.alhalel.topten.enteties.Player;
import com.alhalel.topten.model.HomePageModel;
import com.alhalel.topten.services.PlayersService;
import com.alhalel.topten.services.RankingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class HomeController {

    private final PlayersService playersService;

    private final RankingService rankingService;

    @RequestMapping("/test")
    public @ResponseBody String greeting() {
        return "Hello, World";
    }

    @GetMapping("/")
    String index(Model model) {
        model.addAttribute("now", LocalDateTime.now());
        model.addAttribute("homePageModel", new HomePageModel());
        return "index";
    }

    @PostMapping
    public String findPlayer(HomePageModel homePageModel, Model model) {
        Player player = playersService.getPlayer(homePageModel.getPlayerItem());

        rankingService.getRankingListForAccount()
                .flatMap(list -> list.getRankListItems().stream()
                        .filter(i -> i.getPlayer().equals(player))
                        .findFirst())
                .ifPresent(item -> model.addAttribute("rankingItem", item));

        model.addAttribute("homePageModel", homePageModel);
        model.addAttribute("player", player);
        return "index";
    }

}
