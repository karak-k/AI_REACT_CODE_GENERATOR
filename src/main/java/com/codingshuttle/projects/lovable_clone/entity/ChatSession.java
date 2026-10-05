package com.codingshuttle.projects.lovable_clone.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="chat_sessions")
@Builder
public class ChatSession {

    @EmbeddedId
    private ChatSessionId Id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
            @MapsId("projectId")
    Project project;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
            @MapsId("userId")
    User user;

    String title;

    @CreationTimestamp
    Instant createdAt;

    @UpdateTimestamp
    Instant updatedAt;

    Instant deletedAt; //soft delete
}
