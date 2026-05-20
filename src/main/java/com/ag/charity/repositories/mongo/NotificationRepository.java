package com.ag.charity.repositories.mongo;

import com.ag.charity.entities.mongo.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

    List<Notification> findByRecipientEmailOrderByCreatedAtDesc(String email);

    List<Notification> findByRecipientEmailAndReadFalse(String email);
}
