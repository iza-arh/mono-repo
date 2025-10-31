package com.ues.parcial.repositories;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ues.parcial.Models.ReportPhotos;

public interface ReportPhotoRepository extends JpaRepository<ReportPhotos, UUID> {

    // *--- Basic Filters ---*
    // *All photos associated with a report by report ID*
    List<ReportPhotos> findByReport_Id(UUID reportId);

    // *All photos uploaded by a specific user by user ID*
    List<ReportPhotos> findByUploadedBy_Id(String userId);

    // *All photos uploaded within a specific date range*
    List<ReportPhotos> findByUploadedAtBetween(OffsetDateTime start, OffsetDateTime end);

    // *Find a photo by its URL*
    Optional<ReportPhotos> findByUrl(String url);

    // *--- Orderings ---*
    // *All photos ordered by upload date descending, most recent first*

    List<ReportPhotos> findAllByOrderByUploadedAtDesc();

    // *--- Custom Searches ---*
    // *Count photos associated with a specific report by report ID*
    long countByReport_Id(UUID reportId);

    // *Methods only for SuperAdmin use ---------------------------------------------------*
    // *Delete all photos associated with a specific report by report ID (bulk delete)*
    @Modifying
    @Query("DELETE FROM ReportPhotos p WHERE p.report.id = :reportId")
    void deleteByReportId(@Param("reportId") UUID reportId);

    // *Delete all photos uploaded by a specific user by user ID (bulk delete)*
    @Modifying
    @Query("DELETE FROM ReportPhotos p WHERE p.uploadedBy.id = :userId")
    void deleteByUploadedById(@Param("userId") String userId);

}