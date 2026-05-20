package com.ag.charity.controller.mvc;

import com.ag.charity.entities.enums.Category;
import com.ag.charity.service.CharityActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final CharityActionService charityActionService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("featuredActions", charityActionService.getFeaturedActions());
        model.addAttribute("categories", Category.values());
        return "index";
    }

    @GetMapping("/explore")
    public String explore(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Category category,
            Model model) {
        model.addAttribute("actions", charityActionService.searchActions(keyword, category));
        model.addAttribute("categories", Category.values());
        model.addAttribute("selectedCategory", category);
        model.addAttribute("keyword", keyword);
        return "explore";
    }
}
