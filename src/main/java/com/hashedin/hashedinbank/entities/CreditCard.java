package com.hashedin.hashedinbank.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hashedin.hashedinbank.converter.AttributeEncryptor;
import com.hashedin.hashedinbank.logic.MaskCCNumberLogic;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(name = "CREDIT_CARD", uniqueConstraints = {@UniqueConstraint(columnNames = "CC_NO", name = "XAK1CREDIT_CARD")})
@DynamicInsert
public class CreditCard extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CC_ID", nullable = false, updatable = false)
    private Long creditCardId;

    @Getter(AccessLevel.NONE)
    @Column(name = "CC_NO", nullable = false, updatable = false)
    @Convert(converter = AttributeEncryptor.class)
    private String creditCardNumber;

    @Column(name = "ISSUED_DT", nullable = false, updatable = false)
    private LocalDate issuedDate;

    @Column(name = "EXPIRED_DT", nullable = false, updatable = false)
    private LocalDate expiredDate;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "CVV", nullable = false, updatable = false)
    @Convert(converter = AttributeEncryptor.class)
    private String cvv;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_ID", nullable = false, updatable = false)
    @JsonIgnore
    private User cardHolderId;

    @Column(name = "IS_ACTIVE_FLG", nullable = false)
    @ColumnDefault(value = "true")
    private boolean isActiveFlag;

    public String getCreditCardNumber() {
        return MaskCCNumberLogic.maskStringField('*', creditCardNumber);
    }
}