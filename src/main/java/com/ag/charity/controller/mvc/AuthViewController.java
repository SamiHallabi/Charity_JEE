package com.ag.charity.controller.mvc;

import com.ag.charity.DTO.OrganizationRegisterRequestDTO;
import com.ag.charity.DTO.RegisterRequestDTO;
import com.ag.charity.service.OrganizationService;
import com.ag.charity.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthViewController {

    private final UserService userService;
    private final OrganizationService organizationService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("userForm", new RegisterRequestDTO());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("userForm") RegisterRequestDTO dto,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) return "auth/register";
        try {
            userService.register(dto);
            return "redirect:/auth/login?registered";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }
    }

    @GetMapping("/register-org")
    public String registerOrgPage(Model model) {
        model.addAttribute("orgForm", new OrganizationRegisterRequestDTO());
        return "auth/register-org";
    }

    @PostMapping("/register-org")
    public String registerOrg(
            @Valid @ModelAttribute("orgForm") OrganizationRegisterRequestDTO dto,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) return "auth/register-org";
        try {
            organizationService.register(dto);
            return "redirect:/auth/login?org-registered";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register-org";
        }
    }
}
