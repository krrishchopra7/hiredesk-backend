package com.hiredesk.repository;

import com.hiredesk.model.JobOpening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<JobOpening, Long> {

    List<JobOpening> findByCreatedById(Long userId);

    List<JobOpening> findByStatus(JobOpening.JobStatus status);

    List<JobOpening> findByDepartment(String department);
}