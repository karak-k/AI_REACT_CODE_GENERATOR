package com.codingshuttle.projects.lovable_clone.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public interface AiGenerationService {

    Flux<String> streamResponse(String message, Long projectId);
}
