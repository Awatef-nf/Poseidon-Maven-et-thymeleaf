package com.poseidon.controllers;

import com.poseidon.domain.RuleName;
import com.poseidon.service.RuleNameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
public class RuleNameController {
    @Autowired
    private RuleNameService ruleNameService;


    @RequestMapping("/ruleName/list")
    public String home(Model model)
    {model.addAttribute("ruleNames",ruleNameService.findAll());
        return "ruleName/list";
    }

    @GetMapping("/ruleName/add")
    public String addRuleForm(Model model) {
        model.addAttribute("ruleName",new RuleName());
        return "ruleName/add";
    }

    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName,
                           @ModelAttribute("ruleName")
                           BindingResult result) {

        if (result.hasErrors()) {
            return "ruleName/add";
        }
        ruleNameService.addRuleName(ruleName);
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable Integer id, Model model) {
        RuleName ruleName = ruleNameService.findById(id);
        model.addAttribute("ruleName",ruleName);
        return "ruleName/update";
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable Integer id,
                                 @Valid @ModelAttribute("ruleName") RuleName ruleName,
                                 BindingResult result, Model model) {
        if(result.hasErrors()){
            model.addAttribute("ruleName",ruleName);
            return "ruleName/update";
        }
        ruleName.setId(id);
        ruleNameService.addRuleName(ruleName);
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable Integer id, Model model) {
        ruleNameService.deleteById(id);
        return "redirect:/ruleName/list";
    }
}
