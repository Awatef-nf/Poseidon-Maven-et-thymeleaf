package com.poseidon.controllers;

import com.poseidon.domain.Trade;
import com.poseidon.service.TradeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
public class TradeController {
    @Autowired
    private TradeService tradeService;

    @RequestMapping("/trade/list")
    public String home(Model model)
    { model.addAttribute("trades",tradeService.findAll());
        return "trade/list";
    }

    @GetMapping("/trade/add")
    public String addUser(Model model) {
        model.addAttribute("trade",new Trade());
        return "trade/add";
    }

    @PostMapping("/trade/validate")
    public String validate(@Valid @ModelAttribute("trade") Trade trade,
                           BindingResult result
                           ) {

        if (result.hasErrors()) {
            return "trade/add";
        }
        tradeService.addTrade(trade);
        return "redirect:/trade/list";
    }

    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable Integer id, Model model) {
       Trade trade =tradeService.findById(id);
       model.addAttribute("trade",trade);
        return "trade/update";
    }

    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable Integer id,
                              @Valid Trade trade,
                              BindingResult result,
                              Model model) {
        if(result.hasErrors()){
            model.addAttribute("trade",trade);
            return "trade/update";
        }
        trade.setTradeId(id);
        tradeService.addTrade(trade);
        return "redirect:/trade/list";
    }

    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable Integer id, Model model) {
        tradeService.deleteById(id);
        return "redirect:/trade/list";
    }
}
