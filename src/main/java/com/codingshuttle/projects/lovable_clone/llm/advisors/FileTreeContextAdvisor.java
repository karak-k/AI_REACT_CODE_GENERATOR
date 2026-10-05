package com.codingshuttle.projects.lovable_clone.llm.advisors;

import com.codingshuttle.projects.lovable_clone.dto.project.FileNode;
import com.codingshuttle.projects.lovable_clone.service.ProjectFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.prompt.Prompt;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class FileTreeContextAdvisor implements StreamAdvisor {

    private ProjectFileService projectFileService;
    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest request, StreamAdvisorChain streamAdvisorChain) {
       Long projectId=Long.parseLong(request.context().getOrDefault("projectId",0).toString());



        ChatClientRequest augmentedChatClientRequest= augmentRequestWithFileTree(request,projectId );

        return streamAdvisorChain.nextStream(augmentedChatClientRequest);

    }

    private ChatClientRequest augmentRequestWithFileTree(ChatClientRequest request, Long projectId) {
        List<FileNode> fileTree= projectFileService.getFileTree(projectId);
       String fileTreeContext= "\n\n ----FILE_TREE ----\n" +fileTree.toString();

        List<Message> incomingMessages =request.prompt().getInstructions();

        Message systemMessage = incomingMessages.stream()
                .filter(m->m.getMessageType()== MessageType.SYSTEM)
                .findFirst()
                .orElse(null);

        List<Message> userMessage= incomingMessages.stream()
                .filter(m->m.getMessageType()!=MessageType.SYSTEM)
                .toList();

        List<Message> allMessages = new ArrayList<>();
        //Add Original System Message

        if(systemMessage!=null)
        {
            allMessages.add(systemMessage);
        }

        allMessages.add(new SystemMessage(fileTreeContext));
        allMessages.addAll(userMessage);

      return   request.mutate()
                .prompt(new Prompt(allMessages, request.prompt().getOptions()))
                .build();
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
