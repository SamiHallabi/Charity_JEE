package com.ag.charity.service;

import com.ag.charity.entities.mongo.Notification;

import java.util.List;

public interface NotificationService {
    Notification createNotification(String recipientEmail, String type, String subject, String message);
    List<Notification> getNotificationsByEmail(String email);
    List<Notification> getUnreadByEmail(String email);
    void markAsRead(String notificationId);
}
