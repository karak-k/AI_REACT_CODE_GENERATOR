package com.codingshuttle.projects.lovable_clone.service.impl;

import com.codingshuttle.projects.lovable_clone.entity.ChatMessage;
import com.codingshuttle.projects.lovable_clone.entity.ChatResponse;
import com.codingshuttle.projects.lovable_clone.entity.ChatSession;
import com.codingshuttle.projects.lovable_clone.entity.ChatSessionId;
import com.codingshuttle.projects.lovable_clone.mapper.ChatResponseMapper;
import com.codingshuttle.projects.lovable_clone.repository.ChatMessageRepository;
import com.codingshuttle.projects.lovable_clone.repository.ChatSessionRepository;
import com.codingshuttle.projects.lovable_clone.security.AuthUtil;
import com.codingshuttle.projects.lovable_clone.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private AuthUtil authUtil;
    private ChatSessionRepository chatSessionRepository;
    private ChatMessageRepository chatMessageRepository;
    private ChatResponseMapper chatResponseMapper;
    @Override
    public List<ChatResponse> getProjectChatHistory(Long projectId) {
        Long userId = authUtil.getCurrentUserId();

        ChatSession chatSession= chatSessionRepository.getReferenceById(
                new ChatSessionId(projectId, userId)
        );

        List<ChatMessage> chatMessageList = chatMessageRepository.findByChatSession(chatSession);

        return chatResponseMapper.fromChatMessageList(chatMessageList);
    }
}
