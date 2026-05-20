package com.ag.charity.controller.mvc;

import com.ag.charity.DTO.UpdateProfileDTO;
import com.ag.charity.entities.jpa.User;
import com.ag.charity.repositories.jpa.UserRepository;
import com.ag.charity.service.DonationService;
import com.ag.charity.service.ParticipationService;
import com.ag.charity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserViewController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final DonationService donationService;
    private final ParticipationService participationService;

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal UserDetails principal, Model model) {
        User user = userRepository.findByEmail(principal.getUsername());
        if (user == null) return "redirect:/auth/login";
        model.addAttribute("user", user);
        model.addAttribute("updateForm", new UpdateProfileDTO());
        model.addAttribute("donations", donationService.getDonationsByUser(user.getId()));
        return "user/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(
            @AuthenticationPrincipal UserDetails principal,
            @ModelAttribute UpdateProfileDTO dto,
            Model model) {
        User user = userRepository.findByEmail(principal.getUsername());
        try {
            userService.updateProfile(user.getId(), dto);
            return "redirect:/user/profile?updated";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "user/profile";
        }
    }

    @GetMapping("/donations")
    public String donationHistory(@AuthenticationPrincipal UserDetails principal, Model model) {
        User user = userRepository.findByEmail(principal.getUsername());
        model.addAttribute("donations", donationService.getDonationsByUser(user.getId()));
        return "user/donation-history";
    }

    @GetMapping("/participations")
    public String participations(@AuthenticationPrincipal UserDetails principal, Model model) {
        User user = userRepository.findByEmail(principal.getUsername());
        model.addAttribute("participations", participationService.getParticipationsByUser(user.getId()));
        return "user/participations";
    }
}
