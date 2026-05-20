package com.ag.charity.controller.mvc;

import com.ag.charity.DTO.DonationRequestDTO;
import com.ag.charity.entities.jpa.User;
import com.ag.charity.repositories.jpa.UserRepository;
import com.ag.charity.service.CharityActionService;
import com.ag.charity.service.DonationService;
import com.ag.charity.service.MediaService;
import com.ag.charity.service.ParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/actions")
@RequiredArgsConstructor
public class ActionViewController {

    private final CharityActionService charityActionService;
    private final DonationService donationService;
    private final ParticipationService participationService;
    private final MediaService mediaService;
    private final UserRepository userRepository;

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id,
                         @AuthenticationPrincipal UserDetails principal,
                         Model model) {
        model.addAttribute("action", charityActionService.getActionById(id));
        model.addAttribute("medias", mediaService.getMediaByAction(id));
        model.addAttribute("donations", donationService.getDonationsByAction(id));

        if (principal != null) {
            User user = userRepository.findByEmail(principal.getUsername());
            if (user != null) {
                model.addAttribute("isParticipating",
                        participationService.isParticipating(user.getId(), id));
                model.addAttribute("currentUserId", user.getId());
            }
        }
        model.addAttribute("donationForm", new DonationRequestDTO());
        return "actions/detail";
    }

    @PostMapping("/{id}/donate")
    public String donate(@PathVariable Long id,
                         @AuthenticationPrincipal UserDetails principal,
                         @ModelAttribute DonationRequestDTO dto) {
        if (principal == null) return "redirect:/auth/login";
        User user = userRepository.findByEmail(principal.getUsername());
        if (user == null) return "redirect:/auth/login";
        String checkoutUrl = donationService.createStripeCheckoutUrl(
                user.getId(), id, dto.getAmount(), dto.getPaymentMethod());
        return "redirect:" + checkoutUrl;
    }

    @GetMapping("/{id}/donate/success")
    public String donateSuccess(@PathVariable Long id,
                                @RequestParam("session_id") String sessionId) {
        donationService.completeStripePayment(sessionId);
        return "redirect:/actions/" + id + "?donated";
    }

    @PostMapping("/{id}/participate")
    public String participate(@PathVariable Long id,
                              @AuthenticationPrincipal UserDetails principal) {
        if (principal == null) return "redirect:/auth/login";
        User user = userRepository.findByEmail(principal.getUsername());
        if (user == null) return "redirect:/auth/login";
        participationService.participate(user.getId(), id);
        return "redirect:/actions/" + id + "?participated";
    }
}
