package com.ues.parcial.models;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.ues.parcial.models.enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "assignments")
public class Assignments {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "assignment_id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    @ManyToOne
    @JoinColumn(name = "assigned_by")
    private User assignedBy;

    @ManyToMany
    @JoinTable(name = "assignment_technicians", joinColumns = @JoinColumn(name = "assignment_id"), inverseJoinColumns = @JoinColumn(name = "technician_id"))
    private List<User> technicians;

    @Column(name = "assigned_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime assignedAt;

    @Column(name = "accepted_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime acceptedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "report_state")
    private Status status = Status.ASSIGNED;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> metadata;

    @PrePersist
    public void prePersist() {
        if (assignedAt == null) {
            assignedAt = OffsetDateTime.now();
        }
    }
}
