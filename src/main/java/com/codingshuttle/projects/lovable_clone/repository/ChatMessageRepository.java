package com.codingshuttle.projects.lovable_clone.repository;

import com.codingshuttle.projects.lovable_clone.entity.ChatMessage;
import com.codingshuttle.projects.lovable_clone.entity.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
     @Query("""
        SELECT DISTINCT m FROM ChatMessage m
             LEFT JOIN FETCH m.events e
             WHERE m.chatSession = :chatSession
            ORDER BY m.createdAt ASC, e.sequenceOrder ASC
     """)
    List<ChatMessage> findByChatSession(ChatSession chatSession);
}

// Here we can face N+1 query problem , if we rely on JPA provided method to find chat message by using chatsession
// so we will right custome query