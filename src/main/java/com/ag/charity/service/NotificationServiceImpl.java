package com.ag.charity.service;

import com.ag.charity.entities.mongo.Notification;
import com.ag.charity.repositories.mongo.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired(required = false)
    private NotificationRepository notificationRepository;

    @Override
    public Notification createNotification(String recipientEmail, String type,
                                           String subject, String message) {
        if (notificationRepository == null) return null;
        Notification n = new Notification();
        n.setRecipientEmail(recipientEmail);
        n.setType(type);
        n.setSubject(subject);
        n.setMessage(message);
        n.setRead(false);
        n.setCreatedAt(LocalDateTime.now());
        return notificationRepository.save(n);
    }

    @Override
    public List<Notification> getNotificationsByEmail(String email) {
        if (notificationRepository == null) return Collections.emptyList();
        return notificationRepository.findByRecipientEmailOrderByCreatedAtDesc(email);
    }

    @Override
    public List<Notification> getUnreadByEmail(String email) {
        if (notificationRepository == null) return Collections.emptyList();
        return notificationRepository.findByRecipientEmailAndReadFalse(email);
    }

    @Override
    public void markAsRead(String notificationId) {
        if (notificationRepository == null) return;
        notificationRepository.findById(notificationId).ifPresent(n -> {
            n.setRead(true);
            notificationRepository.save(n);
        });
    }
}
