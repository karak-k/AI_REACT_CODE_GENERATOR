package com.codingshuttle.projects.lovable_clone.security;


import com.codingshuttle.projects.lovable_clone.enums.ProjectPermissions;
import com.codingshuttle.projects.lovable_clone.enums.ProjectRole;
import com.codingshuttle.projects.lovable_clone.repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.codingshuttle.projects.lovable_clone.enums.ProjectPermissions.*;

@Component("security")   // you can define bean name , if you want to , otherwise bean will have the name of class
@RequiredArgsConstructor
public class SecurityExpressions {

    private final AuthUtil authUtil;
    private final ProjectMemberRepository projectMemberRepository;

    private boolean hasPermission(Long projectId, ProjectPermissions projectPermissions)
    {
        Long UserId= authUtil.getCurrentUserId();
        return  projectMemberRepository.findBYProjectRoleWithProjectIdAndUserId(projectId, UserId)
                .map(role -> role.getPermissions().contains(projectPermissions))
                .orElse(false);
    }

    public boolean canViewProject(Long projectId)
    {
        return hasPermission(projectId,VIEW);

    }

    public boolean canEditProject(Long projectId)
    {
        return hasPermission(projectId,EDIT);
    }

    public boolean canDeleteProject(Long projectId)
    {
        return hasPermission(projectId,DELETE);
    }

    public boolean canViewMembers(Long projectId)
    {
        return hasPermission(projectId,VIEW_MEMBERS);
    }

    public boolean canManageMembers(Long projectId)
    {
        return hasPermission(projectId,MANAGE_MEMBERS);
    }
}
