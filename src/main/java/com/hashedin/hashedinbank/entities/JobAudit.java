package com.hashedin.hashedinbank.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(name = "JOB_AUDIT")
public class JobAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "JOB_ID", nullable = false, updatable = false)
    private Integer jobId;

    @Column(name = "JOB_NME", nullable = false)
    private String jobName;

    @Column(name = "START_TS", nullable = false, updatable = false)
    private LocalDateTime startTime;

    @Column(name = "END_TS")
    private LocalDateTime endTime;

    @Column(name = "JOB_STS")
    private String jobStatus;

    @Column(name = "COMMENTS")
    private String comments;
}
