package com.codingshuttle.projects.lovable_clone.enums;

import java.util.Set;
import com.codingshuttle.projects.lovable_clone.enums.ProjectPermissions.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.codingshuttle.projects.lovable_clone.enums.ProjectPermissions.*;

@RequiredArgsConstructor
@Getter
public enum ProjectRole {
    EDITOR(Set.of(VIEW,EDIT, DELETE, VIEW_MEMBERS)),
    VIEWER(Set.of(VIEW,VIEW_MEMBERS)),
    OWNER(Set.of(VIEW,EDIT,DELETE,MANAGE_MEMBERS, VIEW_MEMBERS));



    private final Set<ProjectPermissions> permissions;
}
