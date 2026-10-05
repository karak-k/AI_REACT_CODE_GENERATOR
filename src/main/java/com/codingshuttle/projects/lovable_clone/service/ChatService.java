package com.codingshuttle.projects.lovable_clone.service;

import com.codingshuttle.projects.lovable_clone.entity.ChatResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChatService {

    List<ChatResponse> getProjectChatHistory(Long projectId);
}
