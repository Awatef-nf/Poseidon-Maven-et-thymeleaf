package com.poseidon.controllers;

import com.poseidon.domain.CurvePoint;
import com.poseidon.service.CurvePointService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
public class CurveController {
    @Autowired
    private CurvePointService curvePointService;

    /* All CurvePoint */
    @GetMapping("/curvePoint/list")
    public String home(Model model)
    {
        model.addAttribute("curvePoints", curvePointService.findAll());
        return "curvePoint/list";
    }
    /* Add */
    @GetMapping("/curvePoint/add")
    public String addCurvePoint(Model model) {
        model.addAttribute("curvePoint",new CurvePoint());
        return "curvePoint/add";
    }

    @PostMapping("/curvePoint/validate")
    public String validate(@Valid @ModelAttribute(name="curvePoint")
                           CurvePoint curvePoint,
                           BindingResult result,
                           Model model) {
        if(result.hasErrors()){
            model.addAttribute("curvePoint",curvePoint);
            return "curvePoint/add";
        }
        curvePointService.addCurvePoint(curvePoint);
        return "redirect:/curvePoint/list";
    }

    /* UpDate */
    @GetMapping("/curvePoint/update/{id}")
    public String showUpDateCurve(@PathVariable Integer id, Model model) {
        CurvePoint curvePoint = curvePointService.findById(id);
        model.addAttribute("curvePoint",curvePoint);
        return "curvePoint/update";
    }

    @PostMapping("/curvePoint/update/{id}")
    public String saveUpdateCurve(@PathVariable Integer id, @Valid CurvePoint curvePoint,
                             BindingResult result, Model model) {
        if(result.hasErrors()){
            model.addAttribute("curvePoint",curvePoint);
            return "curvePoint/update";
        }
        curvePoint.setCurveId(id);
        curvePointService.addCurvePoint(curvePoint);
        return "redirect:/curvePoint/list";
    }
    /* Delete */
    @GetMapping("/curvePoint/delete/{id}")
    public String deleteCurve(@PathVariable Integer id, Model model) {
        curvePointService.deleteById(id);
        return "redirect:/curvePoint/list";
    }
}
