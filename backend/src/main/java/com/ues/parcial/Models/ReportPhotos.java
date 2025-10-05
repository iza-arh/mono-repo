package com.ues.parcial.Models;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "report_photos")
public class ReportPhotos {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "report_photo_id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User uploadedBy;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String url;

    @Column(columnDefinition = "TEXT", name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(columnDefinition = "TEXT", name = "mime_type")
    private String mimeType;

    @Column(columnDefinition = "BIGINT", name = "size_bytes")
    private Long sizeBytes;

    @Column(name = "uploaded_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime uploadedAt;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> metadata;

    @PrePersist
    public void prePersist() {
        if (uploadedAt == null) {
            uploadedAt = OffsetDateTime.now();
        }
    }
}
