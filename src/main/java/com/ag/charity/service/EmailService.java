package com.ag.charity.service;

import java.math.BigDecimal;

public interface EmailService {
    void sendWelcomeEmail(String to, String name);
    void sendDonationConfirmation(String to, BigDecimal amount, String actionTitle);
    void sendOrgApprovalEmail(String to, String orgName, boolean approved, String reason);
    void sendActionUpdateEmail(String to, String actionTitle, String update);
}
