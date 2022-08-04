package com.alhalel.topten.controllers;

import com.alhalel.topten.security.UserPrincipal;
import com.alhalel.topten.social.comments.Comments;
import com.alhalel.topten.social.comments.CommentsService;
import com.alhalel.topten.social.comments.model.CommentModel;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static com.alhalel.topten.controllers.PlayersController.principalToUserPrincipal;

@RequestMapping("comments")
@AllArgsConstructor
@Controller
public class CommentsController {

    private final CommentsService commentsService;


    @GetMapping("/{object}/{objetId}")
    public String getCommentsForObject(
            @PathVariable("object") String object,
            @PathVariable("objetId") long objetId,
            Model model) {

        Comments.CommentsObject commentsObject = Comments.CommentsObject.valueOf(object.toUpperCase());

        List<CommentModel> comments = commentsService.getCommentsForObject(commentsObject, objetId);

        model.addAttribute("comments", comments);

        return "comments/comments :: content-f";
    }

    @Secured("ROLE_USER")
    @PostMapping("/{object}/{objetId}")
    public @ResponseBody CommentModel submitCommentForObject(
            @PathVariable("object") String object,
            @PathVariable("objetId") long objetId,
            @RequestParam("content") String content,
            @RequestParam(name = "parentId", required = false) Long parentId,
            Principal p) {

        UserPrincipal principal = principalToUserPrincipal(p);

        Comments.CommentsObject commentsObject = Comments.CommentsObject.valueOf(object.toUpperCase());

        String cleanContent = Jsoup.clean(content, Safelist.basic());

        return commentsService.updateComment(principal.getUser(), commentsObject, objetId, cleanContent, parentId);
    }

}
