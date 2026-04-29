package com.poseidon.controllers;

import com.poseidon.domain.User;
import com.poseidon.repositories.UserRepository;
import com.poseidon.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@AllArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/user/list")
    public String home(Model model)
    {
        model.addAttribute("users", userService.findAll());
        return "user/list";
    }

    @GetMapping("/user/add")
    public String addUser(Model model) {
        model.addAttribute("user",new User());
        return "user/add";
    }

    @PostMapping("/user/validate")
    public String validate(@Valid @ModelAttribute User user,
                           BindingResult result,
                           Model model) {

        if (result.hasErrors()) {
            return "user/add";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));

        userService.addUser(user);

        return "redirect:/user/list";
    }

    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable Integer id, Model model) {
        try {
            User user = userService.findById(id);
            model.addAttribute("user", user);

        }catch (Exception e){
            System.out.println("erreur");
        }return "user/update";

    }

    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable Integer id,
                             @Valid @ModelAttribute("user") User user,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("user",user);
            return "user/update";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setId(id);
        userService.addUser(user);
        return "redirect:/user/list";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable Integer id, Model model) {
        try {
            userService.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/user/list";
    }
}
