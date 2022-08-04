package com.alhalel.topten.controllers;

import com.alhalel.topten.config.RankingConfig;
import com.alhalel.topten.player.Player;
import com.alhalel.topten.player.PlayersService;
import com.alhalel.topten.ranking.RankList;
import com.alhalel.topten.ranking.RankingService;
import com.alhalel.topten.security.UserPrincipal;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.alhalel.topten.controllers.PlayersController.principalToUserPrincipal;

@Controller
@RequestMapping("ranking")
@AllArgsConstructor
public class RankingController {

    private final RankingService rankingService;

    private final PlayersService playersService;

    private final RankingConfig rankingConfig;

    @GetMapping(value = {"", "/", "/editors-choice"})
    String editorsChoice(Model model) {
        return "redirect:/ranking/lists/" + rankingConfig.getEditorsChoice();
    }

    @GetMapping("/lists")
    String allRankings(
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "updateDateTime") String sortBy,
            Model model) {

        Pageable paging = PageRequest.of(pageNo - 1, pageSize, Sort.by(sortBy).descending());

        Page<RankList> page = rankingService.findAll(paging);

        int totalPages = page.getTotalPages();

        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("page", page);
        return "ranking/all-rankings";
    }

    @GetMapping("/lists/{rankListId}")
    String getList(@PathVariable("rankListId") long rankListId, Model model) {

        rankingService.findRankList(rankListId).ifPresent(list -> {

            switch (list.getVisibility()) {
                case PRIVATE:
                    break;
                case SHARE:
                    model.addAttribute("user", rankingService.getUserForList(list));
                case SHARE_ANONYMOUSLY:
                    model.addAttribute("rankListStats", rankingService.getRankListStatistics(list));
                    model.addAttribute("rankList", list);
                    break;
            }
        });
        return "ranking/ranking";
    }


    @Secured("ROLE_USER")
    @GetMapping("/my-list")
    String myRanking(Principal p, Model model) {
        UserPrincipal principal = principalToUserPrincipal(p);
        rankingService.getRankingListForUser(principal.getId()).ifPresent(list -> {
            model.addAttribute("rankListStats", rankingService.getRankListStatistics(list));
            model.addAttribute("rankList", list);
        });
        return "ranking/my-ranking";
    }


    @Secured("ROLE_USER")
    @PostMapping("/my-list")
    String changeListVisibility(
            @RequestParam(value = "visibility") RankList.RankListVisibility visibility,
            Principal p) {

        UserPrincipal principal = principalToUserPrincipal(p);

        RankList rankList = rankingService.updateRankingListVisibility(principal.getId(), visibility);

        return "redirect:/ranking/my-list";
    }


    @Secured("ROLE_USER")
    @PostMapping("/my-list/update")
    public String updatePlayerRanking(
            @RequestParam(value = "playerId") String playerId,
            @RequestParam(value = "rank") Integer rank,
            Principal p) {

        UserPrincipal principal = principalToUserPrincipal(p);

        Player player = playersService.getPlayer(playerId);

        RankList rankList = rankingService.getRankingListForUser(principal.getId())
                .orElseGet(() -> rankingService.createRankingListForUser(principal.getUser()));

        rankingService.updatePlayerRanking(rankList, player, rank);

        return "redirect:/ranking/my-list";
    }


    @Secured("ROLE_USER")
    @PostMapping("/my-list/remove")
    public String removePlayerRanking(
            @RequestParam(value = "playerId") String playerId,
            Principal p) {

        UserPrincipal principal = principalToUserPrincipal(p);

        Player player = playersService.getPlayer(playerId);

        rankingService.getRankingListForUser(principal.getId())
                .ifPresent(list -> rankingService.removePlayerRanking(list, player));

        return "redirect:/ranking/my-list";
    }
}
