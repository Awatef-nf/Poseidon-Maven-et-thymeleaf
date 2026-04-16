package com.poseidon.controllers;

import com.poseidon.domain.BidList;
import com.poseidon.service.BidListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.model.IModel;


@Controller
@RequiredArgsConstructor
public class BidListController {

    @Autowired
    private BidListService bidListService;

    /* All bidList */
    @GetMapping("/bidList/list")
    public String home(Model model) {
        model.addAttribute("bidLists", bidListService.findAll());
        return "bidList/list";
    }

    /* Add */
    @GetMapping("/bidList/add")
    public String addBidList(Model model) {
        model.addAttribute("bidList",new BidList());
        return "bidList/add";
    }

    @PostMapping("/bidList/validate")
    public String saveBidList(
            @Valid  BidList bidList,
            @ModelAttribute(name ="bidList")
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()){
            return "bidList/add";
        }
        bidListService.addBidlist(bidList);
        return"redirect:/bidList/list";
    }

    /* UpDate */
    @GetMapping("/bidList/update/{id}")
    public String showUpdateBidList(@PathVariable Integer id, Model model) {

        BidList bidList = bidListService.findById(id);
        model.addAttribute("bidList", bidList);
        return "bidList/update";
    }

    @PostMapping("/bidList/update/{id}")
    public String saveUpdateBidList(@PathVariable Integer id,
                                    @Valid BidList bidList,
                                    BindingResult bindingResult,
                                    Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("bidList", bidList);
            return "bidList/update";
        }

        bidList.setBidListId(id);
        bidListService.addBidlist(bidList);
        return "redirect:/bidList/list";
    }

    /* Delete */
    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable Integer id) {
        bidListService.deleteById(id);
        return "redirect:/bidList/list";
    }
}
