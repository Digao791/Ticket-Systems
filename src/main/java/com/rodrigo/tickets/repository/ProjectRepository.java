package com.rodrigo.tickets.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rodrigo.tickets.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

}
