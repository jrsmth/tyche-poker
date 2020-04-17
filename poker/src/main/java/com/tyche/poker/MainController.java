package com.tyche.poker;

import com.tyche.poker.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.UUID;

@Controller // must be Controller (not RestController) for Thymeleaf...
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping(path="/")
    public String register() {
        return "register.html";
    }

    @GetMapping(path="/room")
    public String room(Model model,String uuid) {
        System.out.println("UUID: " + uuid);
        User thisUser = getUser(uuid);
        model.addAttribute("userName", "James");
        model.addAttribute("userChips", 1000);

        return "room.html";
    }

    @PostMapping(path = "/newUser")
    public RedirectView newUser(@RequestBody String name) {
        String uuid = UUID.randomUUID().toString();
        User newUser = new User();
        newUser.setUuid(uuid);
        newUser.setName(name);
        newUser.setChips(1000);
        userRepository.save(newUser);

        RedirectView rv = new RedirectView("/room");
        rv.addStaticAttribute("uuid", uuid);
        return rv;
    }

    @ModelAttribute("user")
    public User getUser(String uuid) {
        return userRepository.findByUuid(uuid);
    }

    @GetMapping(path="/all")
    public @ResponseBody
    Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }




}
