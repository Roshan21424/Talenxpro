package com.talentxpro.website.Entities;

import com.talentxpro.website.Entities.Users.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "verification_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true)
    private String aadhaarCardNumber; // Aadhaar number submitted by the user

    @Column(nullable = true)
    private String documentUrl; // Optional URL for Aadhaar document upload

    @Column(nullable = false)
    private String status = "Pending"; // Pending, Approved, or Rejected

    @Column(nullable = true)
    private String rejectionReason; // Optional field for rejection reason

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createdAt = new Date(); // Timestamp for when the request was created
}
