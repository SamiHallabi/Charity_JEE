package com.ag.charity.entities.mongo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_activities")
public class UserActivity {

    @Id
    private String id;

    private Long userId;

    private String activityType;

    private Long entityId;

    private LocalDateTime timestamp = LocalDateTime.now();
}
