package com.ag.charity.repositories.mongo;

import com.ag.charity.entities.mongo.UserActivity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserActivityRepository extends MongoRepository<UserActivity, String> {

    List<UserActivity> findByUserIdOrderByTimestampDesc(Long userId);

    List<UserActivity> findByUserIdAndActivityType(Long userId, String activityType);
}
