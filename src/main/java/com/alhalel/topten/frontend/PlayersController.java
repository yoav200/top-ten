package com.alhalel.topten.frontend;

import com.alhalel.topten.model.PlayerItem;
import com.alhalel.topten.services.PlayersService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("players")
@AllArgsConstructor
public class PlayersController {

    @Autowired
    private final PlayersService playersService;

    @GetMapping
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
}
