package com.ues.parcial.repositories;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ues.parcial.Models.Category;
import com.ues.parcial.Models.Report;
import com.ues.parcial.Models.User;
import com.ues.parcial.Models.Enums.ReportState;
import com.ues.parcial.Models.Enums.Severity;

public interface ReportRepository extends JpaRepository<Report, UUID> {

    // --- Basic Filters ---
    // finds reports by their state, ex: OPEN, CLOSED, IN_PROGRESS
    List<Report> findByState(ReportState state);

    // finds reports by their severity, ex: LOW, MEDIUM, HIGH
    List<Report> findBySeverity(Severity severity);

    // finds reports by their category ex: pothole 
    List<Report> findByCategory(Category category);
    
    // finds reports by the user who reported them
    List<Report> findByReporter(User reporter);

    // finds reports that are marked as duplicates of other reports
    List<Report> findByDuplicateOfNotNull();

    // finds reports that are active
    List<Report> findByIsActiveTrue();

    // --- Orderings ---
    // most recent reports first
    List<Report> findAllByOrderByCreatedAtDesc();

    // --- Custom Searches ---
    // finds reports created within a specific date range
    @Query("SELECT r FROM Report r WHERE r.createdAt BETWEEN :start AND :end ORDER BY r.createdAt DESC")
    List<Report> findReportsBetweenDates(@Param("start") OffsetDateTime start, @Param("end") OffsetDateTime end);

    // searches reports by keyword in title or description, case insensitive, ex: "pothole", "street light"
    @Query("SELECT r FROM Report r WHERE LOWER(r.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(r.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Report> searchByKeyword(@Param("keyword") String keyword);
}

