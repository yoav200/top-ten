package com.alhalel.topten.controllers;

import com.alhalel.topten.mail.ContactUsModel;
import com.alhalel.topten.mail.MailSenderService;
import com.alhalel.topten.model.MessageHolder;
import com.alhalel.topten.security.UserPrincipal;
import com.alhalel.topten.util.LocalResourceUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

import static com.alhalel.topten.controllers.PlayersController.principalToUserPrincipal;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class HomeController {

    private final LocalResourceUtils localResourceUtils;

    private MailSenderService mailSenderService;


    @RequestMapping("/test")
    public @ResponseBody String greeting() {
        return "Hello, World";
    }

    @GetMapping(value = {"/login"})
    String login() {
        return "login";
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

    @GetMapping(value = {"/contact"})
    String contactUsGet(ContactUsModel contactUsModel, Principal p) {
        if (p != null) {
            UserPrincipal principal = principalToUserPrincipal(p);
            contactUsModel.setName(principal.getName());
            contactUsModel.setEmail(principal.getUsername());
        }
        return "public/contact";
    }

    @PostMapping(value = {"/contact"})
    String contactUsPost(@Valid ContactUsModel contactUsModel, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "public/contact";
        }

        mailSenderService.sendEmail(contactUsModel);

        MessageHolder messageHolder = new MessageHolder();
        messageHolder.getMessages().add(
                MessageHolder.Message.builder()
                        .severity(MessageHolder.Severity.INFO)
                        .title("Your message has been sent")
                        .text("Thank you for contacting us. we'll be in touch.")
                        .build());

        model.addAttribute("messageHolder", messageHolder);
        return "public/contact";
    }

    @Secured("ROLE_USER")
    @GetMapping(value = {"/profile"})
    String profile(Model model) {
        return "profile";
    }


    @GetMapping(value = "/static/bg", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] randomBackgroundImage() throws IOException {
        return localResourceUtils.getRandomBackgroundFile();
    }
}
