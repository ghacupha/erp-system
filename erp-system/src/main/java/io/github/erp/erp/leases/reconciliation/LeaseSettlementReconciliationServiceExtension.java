package io.github.erp.erp.leases.reconciliation;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
import io.github.erp.domain.ApplicationUser;
import io.github.erp.domain.IFRS16LeaseContract;
import io.github.erp.domain.LeasePayment;
import io.github.erp.domain.LeaseRepaymentPeriod;
import io.github.erp.domain.LeaseSettlement;
import io.github.erp.domain.enumeration.ReconciliationStatusType;
import io.github.erp.internal.repository.InternalApplicationUserRepository;
import io.github.erp.internal.service.posting.PostingContext;
import io.github.erp.repository.LeasePaymentRepository;
import io.github.erp.repository.LeaseRepaymentPeriodRepository;
import io.github.erp.repository.LeaseSettlementRepository;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LeaseSettlementReconciliationServiceExtension {

    private static final String MODULE = "LEASE";
    private static final String EVENT_TYPE = "LEASE_SETTLEMENT_RECONCILIATION";
    private static final String TRANSACTION_TYPE = "Lease Settlement Reconciliation";

    private final Logger log = LoggerFactory.getLogger(LeaseSettlementReconciliationServiceExtension.class);

    private final LeaseSettlementRepository leaseSettlementRepository;
    private final LeasePaymentRepository leasePaymentRepository;
    private final LeaseRepaymentPeriodRepository leaseRepaymentPeriodRepository;
    private final InternalApplicationUserRepository applicationUserRepository;

    public LeaseSettlementReconciliationServiceExtension(
        LeaseSettlementRepository leaseSettlementRepository,
        LeasePaymentRepository leasePaymentRepository,
        LeaseRepaymentPeriodRepository leaseRepaymentPeriodRepository,
        InternalApplicationUserRepository applicationUserRepository
    ) {
        this.leaseSettlementRepository = leaseSettlementRepository;
        this.leasePaymentRepository = leasePaymentRepository;
        this.leaseRepaymentPeriodRepository = leaseRepaymentPeriodRepository;
        this.applicationUserRepository = applicationUserRepository;
    }

    public List<PostingContext> reconcilePeriod(Long periodId, Long postedById) {
        log.debug("Reconciling lease settlements for period {}", periodId);
        LeaseRepaymentPeriod period = leaseRepaymentPeriodRepository
            .findById(periodId)
            .orElseThrow(() -> new BadRequestAlertException("Period not found", "leaseSettlement", "periodnotfound"));

        ApplicationUser postedBy = resolvePostedBy(postedById);
        List<LeaseSettlement> settlements = leaseSettlementRepository.findAllByPeriodId(periodId);
        Map<Long, List<LeasePayment>> paymentsByContract = leasePaymentRepository
            .findAllByPaymentDateBetweenAndActiveTrue(period.getStartDate(), period.getEndDate())
            .stream()
            .filter(payment -> payment.getLeaseContract() != null && payment.getLeaseContract().getId() != null)
            .sorted(Comparator.comparing(LeasePayment::getPaymentDate))
            .collect(Collectors.groupingBy(payment -> payment.getLeaseContract().getId()));

        List<PostingContext> contexts = new ArrayList<>();

        for (LeaseSettlement settlement : settlements) {
            LeasePayment expectedPayment = resolveExpectedPayment(settlement, paymentsByContract);
            BigDecimal expectedAmount = expectedPayment != null && expectedPayment.getPaymentAmount() != null
                ? expectedPayment.getPaymentAmount()
                : BigDecimal.ZERO;
            LocalDate expectedDate = expectedPayment != null && expectedPayment.getPaymentDate() != null
                ? expectedPayment.getPaymentDate()
                : period.getEndDate();

            BigDecimal invoiceAmount = settlement.getInvoiceAmount() == null ? BigDecimal.ZERO : settlement.getInvoiceAmount();
            BigDecimal varianceAmount = invoiceAmount.subtract(expectedAmount);

            String varianceType = resolveVarianceType(invoiceAmount, varianceAmount);
            String invoiceTiming = resolveInvoiceTiming(settlement.getSettlementDate(), expectedDate);
            ReconciliationStatusType reconciliationStatus = resolveReconciliationStatus(varianceType, invoiceTiming);
            String varianceReason = resolveVarianceReason(varianceType, invoiceTiming);
            UUID postingId = UUID.randomUUID();

            settlement.setVarianceAmount(varianceAmount);
            settlement.setVarianceReason(varianceReason);
            settlement.setPostingId(postingId);
            settlement.setReconciliationStatus(reconciliationStatus);

            if (shouldEmitPostingContext(reconciliationStatus)) {
                PostingContext.Builder builder = PostingContext
                    .builder()
                    .module(MODULE)
                    .eventType(EVENT_TYPE)
                    .transactionType(TRANSACTION_TYPE)
                    .varianceType(varianceType)
                    .invoiceTiming(invoiceTiming)
                    .amount(resolvePostingAmount(invoiceAmount, varianceAmount))
                    .transactionDate(settlement.getSettlementDate())
                    .description(buildDescription(period, settlement))
                    .postingId(postingId)
                    .postedBy(postedBy);

                enrichContextAttributes(builder, settlement, period, expectedPayment);

                contexts.add(builder.build());
            }
        }

        leaseSettlementRepository.saveAll(settlements);
        return contexts;
    }

    private ApplicationUser resolvePostedBy(Long postedById) {
        if (postedById == null) {
            return null;
        }
        return applicationUserRepository.findById(postedById).orElse(null);
    }

    private LeasePayment resolveExpectedPayment(LeaseSettlement settlement, Map<Long, List<LeasePayment>> paymentsByContract) {
        if (settlement.getLeasePayment() != null) {
            return settlement.getLeasePayment();
        }
        IFRS16LeaseContract leaseContract = settlement.getLeaseContract();
        if (leaseContract == null || leaseContract.getId() == null) {
            return null;
        }
        return paymentsByContract.getOrDefault(leaseContract.getId(), List.of()).stream().findFirst().orElse(null);
    }

    private String resolveVarianceType(BigDecimal invoiceAmount, BigDecimal varianceAmount) {
        if (varianceAmount.compareTo(BigDecimal.ZERO) > 0) {
            return "OVER";
        }
        if (varianceAmount.compareTo(BigDecimal.ZERO) < 0) {
            return invoiceAmount.compareTo(BigDecimal.ZERO) > 0 ? "PARTIAL" : "UNDER";
        }
        return "MATCHED";
    }

    private String resolveInvoiceTiming(LocalDate settlementDate, LocalDate expectedDate) {
        if (settlementDate == null || expectedDate == null) {
            return "UNKNOWN";
        }
        if (settlementDate.isAfter(expectedDate)) {
            return "DELAYED";
        }
        if (settlementDate.isBefore(expectedDate)) {
            return "PREMATURE";
        }
        return "ON_TIME";
    }

    private ReconciliationStatusType resolveReconciliationStatus(String varianceType, String invoiceTiming) {
        if ("MATCHED".equals(varianceType) && "ON_TIME".equals(invoiceTiming)) {
            return ReconciliationStatusType.RECONCILED;
        }
        return ReconciliationStatusType.VARIANCE;
    }

    private String resolveVarianceReason(String varianceType, String invoiceTiming) {
        if ("MATCHED".equals(varianceType) && "ON_TIME".equals(invoiceTiming)) {
            return "MATCHED";
        }
        return List.of(invoiceTiming, varianceType)
            .stream()
            .filter(Objects::nonNull)
            .filter(value -> !"MATCHED".equals(value))
            .filter(value -> !"ON_TIME".equals(value))
            .filter(value -> !"UNKNOWN".equals(value))
            .distinct()
            .collect(Collectors.joining(", "));
    }

    private boolean shouldEmitPostingContext(ReconciliationStatusType status) {
        return status == ReconciliationStatusType.VARIANCE;
    }

    private BigDecimal resolvePostingAmount(BigDecimal invoiceAmount, BigDecimal varianceAmount) {
        if (varianceAmount == null) {
            return invoiceAmount;
        }
        if (varianceAmount.compareTo(BigDecimal.ZERO) == 0) {
            return invoiceAmount;
        }
        return varianceAmount.abs();
    }

    private String buildDescription(LeaseRepaymentPeriod period, LeaseSettlement settlement) {
        String periodCode = Optional.ofNullable(period.getPeriodCode()).orElse("period");
        return "Lease settlement reconciliation for " + periodCode + " invoice " + settlement.getInvoiceReference();
    }

    private void enrichContextAttributes(
        PostingContext.Builder builder,
        LeaseSettlement settlement,
        LeaseRepaymentPeriod period,
        LeasePayment expectedPayment
    ) {
        if (settlement.getId() != null) {
            builder.attribute("leaseSettlementId", settlement.getId().toString());
        }
        if (period.getId() != null) {
            builder.attribute("periodId", period.getId().toString());
        }
        if (settlement.getInvoiceReference() != null) {
            builder.attribute("invoiceReference", settlement.getInvoiceReference());
        }
        if (settlement.getLeaseContract() != null && settlement.getLeaseContract().getId() != null) {
            builder.attribute("leaseContractId", settlement.getLeaseContract().getId().toString());
        }
        if (expectedPayment != null && expectedPayment.getId() != null) {
            builder.attribute("leasePaymentId", expectedPayment.getId().toString());
        }
    }
}
