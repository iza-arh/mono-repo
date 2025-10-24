package com.ues.parcial.Models;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;

import com.ues.parcial.Models.Enums.ReportState;
import com.ues.parcial.Models.Enums.Severity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@org.hibernate.annotations.Check(constraints = "duplicate_of IS NULL OR duplicate_of <> report_id")
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "report_id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "zone_id")
    private Zone zone;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReportPhotos> reportPhotos;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Assignments> assignments;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comments> comments;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReportHistory> reportHistories;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "severity_t")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private Severity severity = Severity.LOW;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "report_state")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private ReportState state = ReportState.REPORTED;

    @ManyToOne
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = false)
    private OffsetDateTime updatedAt;

    @Column(name = "occurred_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime occurredAt;

    @Column(columnDefinition = "geography(Point,4326)", nullable = false)
    @JdbcTypeCode(SqlTypes.GEOGRAPHY)
    private Point location;

    @Column(name = "location_text")
    private String locationText;

    @ManyToOne
    @JoinColumn(name = "duplicate_of")
    private Report duplicateOf;

    @Column(nullable = false)
    private Integer priority = 0;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> metadata;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @PrePersist
    public void prePersist() {
        createdAt = OffsetDateTime.now();
        updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}
