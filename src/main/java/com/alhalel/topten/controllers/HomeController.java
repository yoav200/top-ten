package com.alhalel.topten.controllers;

import com.alhalel.topten.model.MessageHolder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class HomeController {

    @RequestMapping("/test")
    public @ResponseBody String greeting() {
        return "Hello, World";
    }

    @GetMapping(value = {"/", "index"})
    String index(Model model) {
        MessageHolder messageHolder = new MessageHolder();
        messageHolder.getMessages().add(MessageHolder.Message.builder()
                .severity(MessageHolder.Severity.INFO)
                .title("Welcome!")
                .text("Welcome to the NBA Greatest Player Ranker!")
                .build());

        model.addAttribute("messageHolder", messageHolder);
        return "index";
    }

    @GetMapping(value = {"/profile"})
    String profile(Model model) {
        return "profile";
    }

}
