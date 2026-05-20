package com.ag.charity.controller.mvc;

import com.ag.charity.DTO.CharityActionCreateDTO;
import com.ag.charity.DTO.CharityActionUpdateDTO;
import com.ag.charity.DTO.OrganizationProfileUpdateDTO;
import com.ag.charity.entities.enums.ActionStatus;
import com.ag.charity.entities.enums.Category;
import com.ag.charity.entities.jpa.Organization;
import com.ag.charity.repositories.jpa.OrganizationRepository;
import com.ag.charity.service.CharityActionService;
import com.ag.charity.service.MediaService;
import com.ag.charity.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/org")
@RequiredArgsConstructor
public class OrgViewController {

    private final OrganizationService organizationService;
    private final OrganizationRepository organizationRepository;
    private final CharityActionService charityActionService;
    private final MediaService mediaService;

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal UserDetails principal, Model model) {
        Organization org = organizationRepository.findByOrgEmail(principal.getUsername());
        model.addAttribute("org", org);
        model.addAttribute("updateForm", new OrganizationProfileUpdateDTO());
        return "org/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(
            @AuthenticationPrincipal UserDetails principal,
            @ModelAttribute OrganizationProfileUpdateDTO dto,
            Model model) {
        Organization org = organizationRepository.findByOrgEmail(principal.getUsername());
        try {
            organizationService.updateOrganizationProfile(org.getId(), dto);
            return "redirect:/org/profile?updated";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "org/profile";
        }
    }

    @GetMapping("/actions")
    public String actions(@AuthenticationPrincipal UserDetails principal, Model model) {
        Organization org = organizationRepository.findByOrgEmail(principal.getUsername());
        model.addAttribute("actions", charityActionService.getActionsByOrganization(org.getId()));
        model.addAttribute("org", org);
        return "org/actions";
    }

    @GetMapping("/actions/create")
    public String createActionPage(Model model) {
        model.addAttribute("actionForm", new CharityActionCreateDTO());
        model.addAttribute("categories", Category.values());
        return "org/action-form";
    }

    @PostMapping("/actions/create")
    public String createAction(
            @AuthenticationPrincipal UserDetails principal,
            @Valid @ModelAttribute("actionForm") CharityActionCreateDTO dto,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", Category.values());
            return "org/action-form";
        }
        Organization org = organizationRepository.findByOrgEmail(principal.getUsername());
        try {
            charityActionService.createAction(org.getId(), dto);
            return "redirect:/org/actions?created";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("categories", Category.values());
            return "org/action-form";
        }
    }

    @GetMapping("/actions/{id}/edit")
    public String editActionPage(@PathVariable Long id, Model model) {
        model.addAttribute("action", charityActionService.getActionById(id));
        model.addAttribute("updateForm", new CharityActionUpdateDTO());
        model.addAttribute("categories", Category.values());
        model.addAttribute("statuses", ActionStatus.values());
        return "org/action-form";
    }

    @PostMapping("/actions/{id}/edit")
    public String editAction(
            @PathVariable Long id,
            @ModelAttribute CharityActionUpdateDTO dto,
            Model model) {
        try {
            charityActionService.updateAction(id, dto);
            return "redirect:/org/actions?updated";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("categories", Category.values());
            return "org/action-form";
        }
    }

    @PostMapping("/actions/{id}/archive")
    public String archiveAction(@PathVariable Long id) {
        charityActionService.archiveAction(id);
        return "redirect:/org/actions?archived";
    }

    @PostMapping("/actions/{id}/upload")
    public String uploadMedia(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        mediaService.uploadMedia(id, file);
        return "redirect:/actions/" + id + "?uploaded";
    }
}
