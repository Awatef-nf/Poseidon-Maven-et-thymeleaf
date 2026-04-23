package com.poseidon.controllers;

import com.poseidon.domain.BidList;
import com.poseidon.domain.Rating;
import com.poseidon.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
public class RatingController {
    @Autowired
    private RatingService ratingService;
    /* All bidList */
    @GetMapping("/rating/list")
    public String home(Model model)
    {
        model.addAttribute("ratings", ratingService.findAll());
        return "rating/list";
    }
    /* Add */
    @GetMapping("/rating/add")
    public String addRatingForm(Model model) {
        model.addAttribute("rating",new Rating());
        return "rating/add";
    }

    @PostMapping("/rating/validate")
    public String validate(@Valid
                           @ModelAttribute(name ="rating") Rating rating,
                           BindingResult result,
                           Model model) {
        if(result.hasErrors()){
            model.addAttribute("rating",rating);
            return "rating/add";
        }
        ratingService.addRating(rating);
        return "redirect:/rating/list";
    }
    /* UpDate */
    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable Integer id, Model model) {
        try {
           Rating rate  = ratingService.findById(id);
            model.addAttribute("rating", rate);
            return "rating/update";
        } catch (IllegalArgumentException e) {
            return "redirect:/rating/list";
        }
    }

    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable Integer id,
                               @Valid @ModelAttribute("rating") Rating rating,
                               BindingResult result,
                               Model model) {

        if (result.hasErrors()) {
            model.addAttribute("rating", rating);
            return "rating/update";
        }
        rating.setId(id);
        ratingService.addRating(rating);
        return "redirect:/rating/list";
    }

    /* Delete */
    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable Integer id, Model model) {
        try {
            ratingService.deleteById(id);
        } catch (IllegalArgumentException e) {
            System.out.println(" not valid id ");;
        }
        return "redirect:/rating/list";
    }
}
