package com.backend.repository.project;

import com.backend.entity.project.ProjectCoworker;
import com.backend.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectCoworkerRepository extends JpaRepository<ProjectCoworker, Long> {
    List<ProjectCoworker> findByUserAndProjectStatusIsNot(User user, int status);
}
