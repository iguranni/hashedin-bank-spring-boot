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
@Table(name = "JOB_ERROR_INFO")
public class JobErrorInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "JOB_ERROR_INFO_ID", nullable = false, updatable = false)
    private Integer jobErrorInfoId;

    @Column(name = "JOB_ID", nullable = false, updatable = false)
    private int jobId;

    @Column(name = "ERROR_DSC",nullable = false,  columnDefinition = "LONGTEXT")
    private String errorDescription;

    @Column(name = "ERROR_STACK_TRACE", columnDefinition = "LONGTEXT")
    private String errorStackTrace;

    @Column(name = "ERROR_TS", nullable = false, updatable = false)
    private LocalDateTime errorTimestamp;
}
