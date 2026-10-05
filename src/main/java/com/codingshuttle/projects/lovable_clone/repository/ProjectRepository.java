package com.codingshuttle.projects.lovable_clone.repository;

import com.codingshuttle.projects.lovable_clone.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository  // not required because sprint boot configure it as repository as it is extended from JPARepository
public interface ProjectRepository extends JpaRepository<Project,Long> {

    // This is cutomise JPQL , which then converted to sql by hibernate
    //@Param use is must because it sync the table attribute name to the argument

    @Query("""
            SELECT p
            FROM Project p
            WHERE p.deletedAt IS NULL
            AND EXISTS(
                  SELECT 1 from ProjectMember pm
                        WHERE pm.id.userId = :userId
                        AND pm.id.projectId = p.id
              )
            ORDER BY p.updatedAt DESC
      """)
    List<Project> findAllAccesibleByUser(@Param("userId") Long UserId);


    @Query("""
           Select p
           From Project p
            WHERE p.id = :projectId
            AND p.deletedAt IS NULL
              AND EXISTS(
                  SELECT 1 from ProjectMember pm
                        WHERE pm.id.userId = :userId
                        AND pm.id.projectId = :projectId
              )     
       """)
    Optional<Project> findAccessibleProjectById(@Param("projectId") Long projectId,
                                                @Param("userId") Long userId);

}
