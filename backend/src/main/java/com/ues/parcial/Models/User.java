package com.ues.parcial.Models;

import java.time.OffsetDateTime;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.ues.parcial.Models.Enums.UserRole;
import com.ues.parcial.exceptions.InvalidPhoneFormatException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "user_id", nullable = false)
    private String id;

    @OneToMany(mappedBy = "uploadedBy")
    private List<ReportPhotos> reportPhotos;

    @OneToMany(mappedBy = "author")
    private List<Comments> comments;

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications;

    @Column(nullable = false, unique = true)
    private String email;

    /*@Column(name = "password_hash", nullable = false)
    private String passwordHash;*/

    @Column(nullable = false)
    private String name;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "user_role")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private UserRole role = UserRole.CITIZEN;

    @Column
    private String phone;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = OffsetDateTime.now();
        updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = OffsetDateTime.now();
    }

    public static String validatePhone(String phone) {
        if (phone == null) return null;
        if (!phone.matches("\\d{8}|\\d{4}-\\d{4}")) {
            throw new InvalidPhoneFormatException(
                "Invalid phone format. Must be 12345678 or 1234-5678"
            );
        }
        return phone;
    }

}
