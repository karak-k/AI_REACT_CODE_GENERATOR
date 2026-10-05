package com.codingshuttle.projects.lovable_clone.service;

import org.springframework.stereotype.Service;

@Service
public interface ProjectTemplateService {

    void intializeProjectFromTemplate(Long projectId);
}
