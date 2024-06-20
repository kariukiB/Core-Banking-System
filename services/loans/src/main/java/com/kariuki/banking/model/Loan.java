package com.kariuki.banking.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.kariuki.banking.model.AccountType.LOANS;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String loanAccount;
    private String relatedAccount;
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime updatedAt;
    private BigDecimal principal;
    private BigDecimal interest;
    private BigDecimal outStandingAmount;
    private BigDecimal installmentAmount;
    private Integer numberOfInstallments;
    private BigDecimal availableFunds;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private AccountType accountType = LOANS;


}
