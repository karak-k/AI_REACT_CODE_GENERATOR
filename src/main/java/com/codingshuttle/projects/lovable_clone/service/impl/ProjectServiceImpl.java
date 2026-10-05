package com.codingshuttle.projects.lovable_clone.service.impl;

import com.codingshuttle.projects.lovable_clone.dto.project.ProjectRequest;
import com.codingshuttle.projects.lovable_clone.dto.project.ProjectResponse;
import com.codingshuttle.projects.lovable_clone.dto.project.ProjectSummaryResponse;
import com.codingshuttle.projects.lovable_clone.entity.Project;
import com.codingshuttle.projects.lovable_clone.entity.ProjectMember;
import com.codingshuttle.projects.lovable_clone.entity.ProjectMemberId;
import com.codingshuttle.projects.lovable_clone.entity.User;
import com.codingshuttle.projects.lovable_clone.enums.ProjectRole;
import com.codingshuttle.projects.lovable_clone.error.BadRequestException;
import com.codingshuttle.projects.lovable_clone.error.ResourceNotFoundException;
import com.codingshuttle.projects.lovable_clone.mapper.ProjectMapper;
import com.codingshuttle.projects.lovable_clone.repository.ProjectMemberRepository;
import com.codingshuttle.projects.lovable_clone.repository.ProjectRepository;
import com.codingshuttle.projects.lovable_clone.repository.UserRepository;
import com.codingshuttle.projects.lovable_clone.security.AuthUtil;
import com.codingshuttle.projects.lovable_clone.service.ProjectService;
import com.codingshuttle.projects.lovable_clone.service.SubscriptionService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@Transactional
public class ProjectServiceImpl implements ProjectService {

    ProjectRepository projectRepository;
    UserRepository   userRepository;
    ProjectMapper projectMapper;
   ProjectMemberRepository projectMemberRepository;
   AuthUtil authUtil;
   SubscriptionService subscriptionService;

    @Override
    public ProjectResponse createProject(ProjectRequest request) {
        if(!subscriptionService.canCreateNewProject())
        {
            throw new BadRequestException("User cannot create a new Project with current plan ! upgrade plan Now.");
        }
       Long userId= authUtil.getCurrentUserId();
//        User Owner= userRepository.findById(userId)
//                .orElseThrow(()->new ResourceNotFoundException("User" ,userId.toString()));

        // no need of making call to DB , we can use Hibernate proxy here to avoid Db call
        // cauzewe just need user Id while saving details of projectMember in below code

        User Owner= userRepository.getReferenceById(userId);
        Project project= Project.builder()
                .name(request.name())
                .isPublic(false)
                .build();

        project =projectRepository.save(project);

        ProjectMemberId memberId=new ProjectMemberId(project.getId(), Owner.getId());
        ProjectMember projectMember= ProjectMember.builder()
                .id(memberId)
                .projectRole(ProjectRole.OWNER)
                .user(Owner)
                .project(project)
                .acceptedAt(Instant.now())
                .invitedAt(Instant.now())
                .build();

        projectMemberRepository.save(projectMember);

        return projectMapper.toProjectResponse((project));
    }
    @Override
    public List<ProjectSummaryResponse> getUserProjects() {
        Long userId= authUtil.getCurrentUserId();
        User owner= userRepository.findById(userId).orElseThrow();
 //       now here is two way to map the response to DTO
//         way1
//        return  projectRepository.getProjectsAccessibleByUser((owner.getId()))
//                 .stream()
//                 .map(project -> projectMapper.toProjectSummaryResponse(project))
//                 .collect(Collectors.toList());

        //way2
       var projects=  projectRepository.findAllAccesibleByUser((owner.getId()));
        return projectMapper.toListProjectSummaryResponse(projects);

    }

    @Override
    @PreAuthorize("@security.canViewProject(#projectId)")
    public ProjectResponse getUserProjectById(Long projectId) {
        Long userId= authUtil.getCurrentUserId();
        Project project=getAccessibleProjectById(projectId, userId);

        return projectMapper.toProjectResponse(project);
    }



    @Override
    @PreAuthorize("@security.canEditProject(#projectId)")
    public ProjectResponse updateProject(Long projectId, ProjectRequest request) {

        Long userId= authUtil.getCurrentUserId();
        Project project=getAccessibleProjectById(projectId, userId);

         project.setName(request.name());

         //Although @Transactional will eventually save this in db no need to save it
        // but still for good practice do that
         projectRepository.save(project);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    @PreAuthorize("@security.canDeleteProject(#projectId)")
    public void softDelete(Long projectId) {
        Long userId= authUtil.getCurrentUserId();
        Project project=getAccessibleProjectById(projectId, userId);

        project.setDeletedAt(Instant.now());
        projectRepository.save(project);

    }

    //Internal Functions
    public Project getAccessibleProjectById(Long projectId ,Long userId)
    {
        return projectRepository.findAccessibleProjectById(projectId,userId)
                .orElseThrow(()->new ResourceNotFoundException("Project", projectId.toString()));
    }
}
