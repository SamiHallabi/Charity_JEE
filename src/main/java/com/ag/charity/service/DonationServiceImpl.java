package com.ag.charity.service;

import com.ag.charity.DTO.DonationRequestDTO;
import com.ag.charity.DTO.DonationResponseDTO;
import com.ag.charity.entities.enums.DonationStatus;
import com.ag.charity.entities.jpa.CharityAction;
import com.ag.charity.entities.jpa.Donation;
import com.ag.charity.entities.jpa.User;
import com.ag.charity.repositories.jpa.CharityActionRepository;
import com.ag.charity.repositories.jpa.DonationRepository;
import com.ag.charity.repositories.jpa.UserRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DonationServiceImpl implements DonationService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    private final DonationRepository donationRepository;
    private final UserRepository userRepository;
    private final CharityActionRepository charityActionRepository;
    private final EmailService emailService;

    public DonationServiceImpl(DonationRepository donationRepository,
                                UserRepository userRepository,
                                CharityActionRepository charityActionRepository,
                                EmailService emailService) {
        this.donationRepository = donationRepository;
        this.userRepository = userRepository;
        this.charityActionRepository = charityActionRepository;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public DonationResponseDTO donate(Long userId, DonationRequestDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        CharityAction action = charityActionRepository.findById(dto.getCharityActionId())
                .orElseThrow(() -> new RuntimeException("Action non trouvée"));

        String transactionId = processPayment(dto);

        Donation donation = new Donation();
        donation.setUser(user);
        donation.setCharityAction(action);
        donation.setAmount(dto.getAmount());
        donation.setPaymentMethod(dto.getPaymentMethod());
        donation.setTransactionId(transactionId);
        donation.setStatus(DonationStatus.COMPLETED);

        Donation saved = donationRepository.save(donation);

        BigDecimal current = action.getAmountCollected() != null ? action.getAmountCollected() : BigDecimal.ZERO;
        action.setAmountCollected(current.add(dto.getAmount()));
        charityActionRepository.save(action);

        emailService.sendDonationConfirmation(user.getEmail(), dto.getAmount(), action.getTitle());

        return toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DonationResponseDTO> getDonationsByUser(Long userId) {
        return donationRepository.findByUserIdOrderByDonationDateDesc(userId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DonationResponseDTO> getDonationsByAction(Long actionId) {
        return donationRepository.findByCharityActionIdOrderByDonationDateDesc(actionId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public String createStripeCheckoutUrl(Long userId, Long actionId, BigDecimal amount, String paymentMethod) {
        Stripe.apiKey = stripeApiKey;
        CharityAction action = charityActionRepository.findById(actionId)
                .orElseThrow(() -> new RuntimeException("Action non trouvée"));
        try {
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(baseUrl + "/actions/" + actionId + "/donate/success?session_id={CHECKOUT_SESSION_ID}")
                    .setCancelUrl(baseUrl + "/actions/" + actionId + "?cancelled")
                    .addLineItem(SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency("mad")
                                    .setUnitAmount(amount.multiply(BigDecimal.valueOf(100)).longValue())
                                    .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                            .setName("Don : " + action.getTitle())
                                            .build())
                                    .build())
                            .build())
                    .putMetadata("userId", String.valueOf(userId))
                    .putMetadata("actionId", String.valueOf(actionId))
                    .putMetadata("amount", amount.toPlainString())
                    .putMetadata("paymentMethod", paymentMethod != null ? paymentMethod : "CARD")
                    .build();
            Session session = Session.create(params);
            return session.getUrl();
        } catch (StripeException e) {
            throw new RuntimeException("Erreur Stripe : " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void completeStripePayment(String sessionId) {
        Stripe.apiKey = stripeApiKey;
        try {
            Session session = Session.retrieve(sessionId);
            if (!"complete".equals(session.getStatus()) && !"paid".equals(session.getPaymentStatus())) {
                throw new RuntimeException("Paiement non finalisé");
            }
            Map<String, String> meta = session.getMetadata();
            Long userId   = Long.parseLong(meta.get("userId"));
            Long actionId = Long.parseLong(meta.get("actionId"));
            BigDecimal amount = new BigDecimal(meta.get("amount"));
            String paymentMethod = meta.get("paymentMethod");

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
            CharityAction action = charityActionRepository.findById(actionId)
                    .orElseThrow(() -> new RuntimeException("Action non trouvée"));

            Donation donation = new Donation();
            donation.setUser(user);
            donation.setCharityAction(action);
            donation.setAmount(amount);
            donation.setPaymentMethod(paymentMethod);
            donation.setTransactionId(session.getPaymentIntent());
            donation.setStatus(DonationStatus.COMPLETED);
            donationRepository.save(donation);

            BigDecimal current = action.getAmountCollected() != null ? action.getAmountCollected() : BigDecimal.ZERO;
            action.setAmountCollected(current.add(amount));
            charityActionRepository.save(action);

            emailService.sendDonationConfirmation(user.getEmail(), amount, action.getTitle());
        } catch (StripeException e) {
            throw new RuntimeException("Erreur Stripe : " + e.getMessage(), e);
        }
    }

    private String processPayment(DonationRequestDTO dto) {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private DonationResponseDTO toDTO(Donation d) {
        return DonationResponseDTO.builder()
                .id(d.getId())
                .amount(d.getAmount())
                .paymentMethod(d.getPaymentMethod())
                .transactionId(d.getTransactionId())
                .status(d.getStatus())
                .donationDate(d.getDonationDate())
                .charityActionId(d.getCharityAction().getId())
                .charityActionTitle(d.getCharityAction().getTitle())
                .userId(d.getUser().getId())
                .userFullName(d.getUser().getFirstName() + " " + d.getUser().getLastName())
                .build();
    }
}
