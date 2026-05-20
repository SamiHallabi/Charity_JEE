package com.ag.charity.service;

import com.ag.charity.DTO.DonationRequestDTO;
import com.ag.charity.DTO.DonationResponseDTO;

import java.math.BigDecimal;
import java.util.List;

public interface DonationService {
    DonationResponseDTO donate(Long userId, DonationRequestDTO dto);
    List<DonationResponseDTO> getDonationsByUser(Long userId);
    List<DonationResponseDTO> getDonationsByAction(Long actionId);
    String createStripeCheckoutUrl(Long userId, Long actionId, BigDecimal amount, String paymentMethod);
    void completeStripePayment(String sessionId);
}
