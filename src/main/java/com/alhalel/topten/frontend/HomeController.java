package com.alhalel.topten.frontend;

import com.alhalel.topten.enteties.Player;
import com.alhalel.topten.model.HomePageModel;
import com.alhalel.topten.services.PlayersService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

@Controller
@RequestMapping({"/", "/index"})
@AllArgsConstructor
public class HomeController {

    private final PlayersService playersService;

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
        model.addAttribute("homePageModel", homePageModel);
        model.addAttribute("player", player);
        return "player-card";
    }

}
