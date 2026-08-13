package com.rodrigo.tickets.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rodrigo.tickets.entity.ProjectMember;
import java.util.List;


public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long>{
    
    List<ProjectMember> findByUserId(Long userId);
    boolean existsByProjectIdAndUserId(Long projectId, Long userId);
}
