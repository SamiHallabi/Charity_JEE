package com.ag.charity.controller.mvc;

import com.ag.charity.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminViewController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("pendingOrgs", adminService.getPendingOrganizations());
        model.addAttribute("allUsers", adminService.getAllUsers());
        model.addAttribute("allOrgs", adminService.getAllOrganizations());
        return "admin/dashboard";
    }

    @GetMapping("/organizations")
    public String organizations(Model model) {
        model.addAttribute("pendingOrgs", adminService.getPendingOrganizations());
        model.addAttribute("allOrgs", adminService.getAllOrganizations());
        return "admin/org-approvals";
    }

    @PostMapping("/organizations/{id}/approve")
    public String approve(@PathVariable Long id) {
        adminService.approveOrganization(id);
        return "redirect:/admin/organizations?approved";
    }

    @PostMapping("/organizations/{id}/reject")
    public String reject(@PathVariable Long id,
                         @RequestParam(required = false) String reason) {
        adminService.rejectOrganization(id, reason);
        return "redirect:/admin/organizations?rejected";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return "redirect:/admin/dashboard?deleted";
    }
}
