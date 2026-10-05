package com.codingshuttle.projects.lovable_clone.mapper;

import com.codingshuttle.projects.lovable_clone.entity.ChatMessage;
import com.codingshuttle.projects.lovable_clone.entity.ChatResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatResponseMapper {
    List<ChatResponse> fromChatMessageList(List<ChatMessage> chatMessageList);
}
