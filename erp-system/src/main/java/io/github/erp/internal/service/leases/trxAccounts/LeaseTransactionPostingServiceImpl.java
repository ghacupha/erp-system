package io.github.erp.internal.service.leases.trxAccounts;

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
import io.github.erp.domain.LeaseLiability;
import io.github.erp.domain.LeaseLiabilityScheduleItem;
import io.github.erp.domain.LeaseRepaymentPeriod;
import io.github.erp.domain.RouDepreciationEntry;
import io.github.erp.domain.RouInitialDirectCost;
import io.github.erp.domain.TransactionDetails;
import io.github.erp.internal.service.posting.PostingContext;
import io.github.erp.internal.service.posting.PostingRuleEvaluator;
import io.github.erp.internal.repository.InternalApplicationUserRepository;
import io.github.erp.internal.repository.InternalLeaseLiabilityRepository;
import io.github.erp.internal.repository.InternalLeaseLiabilityScheduleItemRepository;
import io.github.erp.internal.repository.InternalRouDepreciationEntryRepository;
import io.github.erp.internal.repository.InternalRouInitialDirectCostRepository;
import io.github.erp.internal.repository.InternalTransactionDetailsRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LeaseTransactionPostingServiceImpl implements LeaseTransactionPostingService {

    private static final String LEASE_REPAYMENT_TYPE = "Lease Repayment";
    private static final String LEASE_INTEREST_ACCRUAL_TYPE = "Lease Interest Accrual";
    private static final String LEASE_INTEREST_PAID_TRANSFER_TYPE = "Lease Interest Paid Transfer";
    private static final String LEASE_LIABILITY_RECOGNITION_TYPE = "Lease Liability Recognition";
    private static final String LEASE_ROU_RECOGNITION_TYPE = "ROU Initial Direct Cost Recognition";
    private static final String ROU_AMORTIZATION_TYPE = "ROU Amortization";
    private static final String LEASE_MODULE = "LEASE";
    private static final String LEASE_REPAYMENT_EVENT = "LEASE_REPAYMENT";
    private static final String LEASE_INTEREST_ACCRUAL_EVENT = "LEASE_INTEREST_ACCRUAL";
    private static final String LEASE_INTEREST_PAID_TRANSFER_EVENT = "LEASE_INTEREST_PAID_TRANSFER";
    private static final String LEASE_LIABILITY_RECOGNITION_EVENT = "LEASE_LIABILITY_RECOGNITION";
    private static final String LEASE_ROU_RECOGNITION_EVENT = "LEASE_ROU_RECOGNITION";
    private static final String LEASE_ROU_AMORTIZATION_EVENT = "LEASE_ROU_AMORTIZATION";

    private final InternalTransactionDetailsRepository transactionDetailsRepository;
    private final InternalLeaseLiabilityScheduleItemRepository leaseLiabilityScheduleItemRepository;
    private final InternalLeaseLiabilityRepository leaseLiabilityRepository;
    private final InternalRouInitialDirectCostRepository rouInitialDirectCostRepository;
    private final InternalRouDepreciationEntryRepository rouDepreciationEntryRepository;
    private final InternalApplicationUserRepository applicationUserRepository;
    private final PostingRuleEvaluator postingRuleEvaluator;

    public LeaseTransactionPostingServiceImpl(
        InternalTransactionDetailsRepository transactionDetailsRepository,
        InternalLeaseLiabilityScheduleItemRepository leaseLiabilityScheduleItemRepository,
        InternalLeaseLiabilityRepository leaseLiabilityRepository,
        InternalRouInitialDirectCostRepository rouInitialDirectCostRepository,
        InternalRouDepreciationEntryRepository rouDepreciationEntryRepository,
        InternalApplicationUserRepository applicationUserRepository,
        PostingRuleEvaluator postingRuleEvaluator
    ) {
        this.transactionDetailsRepository = transactionDetailsRepository;
        this.leaseLiabilityScheduleItemRepository = leaseLiabilityScheduleItemRepository;
        this.leaseLiabilityRepository = leaseLiabilityRepository;
        this.rouInitialDirectCostRepository = rouInitialDirectCostRepository;
        this.rouDepreciationEntryRepository = rouDepreciationEntryRepository;
        this.applicationUserRepository = applicationUserRepository;
        this.postingRuleEvaluator = postingRuleEvaluator;
    }

    @Override
    public void postLeaseRepayment(UUID requisitionId, Long postedById) {
        ApplicationUser postedBy = loadPostedBy(postedById);
        List<TransactionDetails> details = leaseLiabilityScheduleItemRepository
            .findAllWithLeaseContractAndPeriod()
            .stream()
            .filter(item -> hasNonZeroAmount(item.getCashPayment()))
            .flatMap(
                item ->
                    postingRuleEvaluator
                        .evaluate(
                            buildLeaseScheduleContext(
                                item,
                                postedBy,
                                requisitionId,
                                LEASE_REPAYMENT_TYPE,
                                "REPAYMENT",
                                item.getCashPayment(),
                                LEASE_REPAYMENT_EVENT
                            )
                        )
                        .stream()
            )
            .collect(Collectors.toList());

        transactionDetailsRepository.saveAll(details);
    }

    @Override
    public void postLeaseInterestAccrual(UUID requisitionId, Long postedById) {
        ApplicationUser postedBy = loadPostedBy(postedById);
        List<TransactionDetails> details = leaseLiabilityScheduleItemRepository
            .findAllWithLeaseContractAndPeriod()
            .stream()
            .filter(item -> hasNonZeroAmount(item.getInterestAccrued()))
            .flatMap(
                item ->
                    postingRuleEvaluator
                        .evaluate(
                            buildLeaseScheduleContext(
                                item,
                                postedBy,
                                requisitionId,
                                LEASE_INTEREST_ACCRUAL_TYPE,
                                "INTEREST ACCRUED",
                                item.getInterestAccrued(),
                                LEASE_INTEREST_ACCRUAL_EVENT
                            )
                        )
                        .stream()
            )
            .collect(Collectors.toList());

        transactionDetailsRepository.saveAll(details);
    }

    @Override
    public void postLeaseInterestPaidTransfer(UUID requisitionId, Long postedById) {
        ApplicationUser postedBy = loadPostedBy(postedById);
        List<TransactionDetails> details = leaseLiabilityScheduleItemRepository
            .findAllWithLeaseContractAndPeriod()
            .stream()
            .filter(item -> hasNonZeroAmount(item.getCashPayment()))
            .flatMap(
                item ->
                    postingRuleEvaluator
                        .evaluate(
                            buildLeaseScheduleContext(
                                item,
                                postedBy,
                                requisitionId,
                                LEASE_INTEREST_PAID_TRANSFER_TYPE,
                                "INTEREST PAID",
                                item.getInterestPayment(),
                                LEASE_INTEREST_PAID_TRANSFER_EVENT
                            )
                        )
                        .stream()
            )
            .collect(Collectors.toList());

        transactionDetailsRepository.saveAll(details);
    }

    @Override
    public void postLeaseLiabilityRecognition(UUID requisitionId, Long postedById) {
        ApplicationUser postedBy = loadPostedBy(postedById);
        List<TransactionDetails> details = leaseLiabilityRepository
            .findAllWithLeaseContract()
            .stream()
            .flatMap(
                liability ->
                    postingRuleEvaluator
                        .evaluate(
                            buildLeaseLiabilityContext(
                                liability,
                                postedBy,
                                requisitionId,
                                LEASE_LIABILITY_RECOGNITION_TYPE,
                                LEASE_LIABILITY_RECOGNITION_EVENT
                            )
                        )
                        .stream()
            )
            .collect(Collectors.toList());

        transactionDetailsRepository.saveAll(details);
    }

    @Override
    public void postLeaseRouRecognition(UUID requisitionId, Long postedById) {
        ApplicationUser postedBy = loadPostedBy(postedById);
        List<TransactionDetails> details = rouInitialDirectCostRepository
            .findAllWithAccounts()
            .stream()
            .flatMap(
                cost ->
                    postingRuleEvaluator
                        .evaluate(
                            buildRouRecognitionContext(cost, postedBy, requisitionId, LEASE_ROU_RECOGNITION_TYPE, LEASE_ROU_RECOGNITION_EVENT)
                        )
                        .stream()
            )
            .collect(Collectors.toList());

        transactionDetailsRepository.saveAll(details);
    }

    @Override
    public void postRouAmortization(UUID requisitionId, Long postedById) {
        ApplicationUser postedBy = loadPostedBy(postedById);
        List<TransactionDetails> details = rouDepreciationEntryRepository
            .findAllWithLeaseContractAndPeriod()
            .stream()
            .flatMap(
                entry ->
                    postingRuleEvaluator
                        .evaluate(
                            buildRouAmortizationContext(
                                entry,
                                postedBy,
                                requisitionId,
                                ROU_AMORTIZATION_TYPE,
                                LEASE_ROU_AMORTIZATION_EVENT
                            )
                        )
                        .stream()
            )
            .collect(Collectors.toList());

        transactionDetailsRepository.saveAll(details);
    }

    private ApplicationUser loadPostedBy(Long postedById) {
        return applicationUserRepository
            .findById(postedById)
            .orElseThrow(() -> new IllegalStateException("Posting user not found for id " + postedById));
    }

    private boolean hasNonZeroAmount(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) != 0;
    }

    private PostingContext buildLeaseScheduleContext(
        LeaseLiabilityScheduleItem item,
        ApplicationUser postedBy,
        UUID requisitionId,
        String transactionType,
        String suffix,
        BigDecimal amount,
        String eventType
    ) {
        IFRS16LeaseContract leaseContract = Objects.requireNonNull(item.getLeaseContract(), "Lease contract is required for schedule entry.");
        LeaseRepaymentPeriod leasePeriod = Objects.requireNonNull(item.getLeasePeriod(), "Lease period is required for schedule entry.");
        Objects.requireNonNull(leasePeriod.getFiscalMonth(), "Fiscal month is required for schedule entry.");
        Objects.requireNonNull(amount, "Amount is required for schedule entry.");
        String fiscalMonthCode = leasePeriod.getFiscalMonth().getFiscalMonthCode();
        String description = leaseContract.getShortTitle() + " " + stripFiscalMonthCode(fiscalMonthCode) + suffix;

        PostingContext.Builder builder = PostingContext
            .builder()
            .module(LEASE_MODULE)
            .eventType(eventType)
            .transactionType(transactionType)
            .transactionDate(leasePeriod.getFiscalMonth().getEndDate())
            .description(description)
            .amount(amount)
            .postingId(requisitionId)
            .postedBy(postedBy)
            .attribute("leaseContractId", leaseContract.getId().toString());
        if (leasePeriod.getId() != null) {
            builder.attribute("leasePeriodId", leasePeriod.getId().toString());
        }
        return builder.build();
    }

    private PostingContext buildLeaseLiabilityContext(
        LeaseLiability leaseLiability,
        ApplicationUser postedBy,
        UUID requisitionId,
        String transactionType,
        String eventType
    ) {
        IFRS16LeaseContract leaseContract = Objects.requireNonNull(leaseLiability.getLeaseContract(), "Lease contract is required for liability.");
        Objects.requireNonNull(leaseLiability.getStartDate(), "Lease liability start date is required.");
        Objects.requireNonNull(leaseLiability.getLiabilityAmount(), "Lease liability amount is required.");
        String description = leaseContract.getShortTitle() + " LEASE RECOGNITION";

        PostingContext.Builder builder = PostingContext
            .builder()
            .module(LEASE_MODULE)
            .eventType(eventType)
            .transactionType(transactionType)
            .transactionDate(leaseLiability.getStartDate())
            .description(description)
            .amount(leaseLiability.getLiabilityAmount())
            .postingId(requisitionId)
            .postedBy(postedBy)
            .attribute("leaseContractId", leaseContract.getId().toString());
        if (leaseLiability.getId() != null) {
            builder.attribute("leaseLiabilityId", leaseLiability.getId().toString());
        }
        return builder.build();
    }

    private PostingContext buildRouRecognitionContext(
        RouInitialDirectCost directCost,
        ApplicationUser postedBy,
        UUID requisitionId,
        String transactionType,
        String eventType
    ) {
        IFRS16LeaseContract leaseContract = Objects.requireNonNull(directCost.getLeaseContract(), "Lease contract is required for ROU cost.");
        Objects.requireNonNull(directCost.getTransactionDate(), "ROU cost transaction date is required.");
        Objects.requireNonNull(directCost.getCost(), "ROU cost amount is required.");
        String reference = directCost.getReferenceNumber() == null ? "" : directCost.getReferenceNumber().toString();
        String description = "Ref#" + reference + " " + leaseContract.getShortTitle() + " INITIAL DIRECT COST";

        PostingContext.Builder builder = PostingContext
            .builder()
            .module(LEASE_MODULE)
            .eventType(eventType)
            .transactionType(transactionType)
            .transactionDate(directCost.getTransactionDate())
            .description(description)
            .amount(directCost.getCost())
            .postingId(requisitionId)
            .postedBy(postedBy)
            .attribute("leaseContractId", leaseContract.getId().toString());
        if (directCost.getId() != null) {
            builder.attribute("rouInitialDirectCostId", directCost.getId().toString());
        }
        return builder.build();
    }

    private PostingContext buildRouAmortizationContext(
        RouDepreciationEntry entry,
        ApplicationUser postedBy,
        UUID requisitionId,
        String transactionType,
        String eventType
    ) {
        Objects.requireNonNull(entry.getDepreciationAmount(), "Depreciation amount is required for ROU amortization.");
        Objects.requireNonNull(entry.getLeasePeriod(), "Lease period is required for ROU amortization.");
        PostingContext.Builder builder = PostingContext
            .builder()
            .module(LEASE_MODULE)
            .eventType(eventType)
            .transactionType(transactionType)
            .transactionDate(entry.getLeasePeriod().getEndDate())
            .description(entry.getDescription())
            .amount(entry.getDepreciationAmount())
            .postingId(requisitionId)
            .postedBy(postedBy);
        if (entry.getLeaseContract() != null) {
            builder.attribute("leaseContractId", entry.getLeaseContract().getId().toString());
        }
        if (entry.getId() != null) {
            builder.attribute("rouDepreciationEntryId", entry.getId().toString());
        }
        return builder.build();
    }

    private String stripFiscalMonthCode(String fiscalMonthCode) {
        return fiscalMonthCode == null ? "" : fiscalMonthCode.replace("YM", "");
    }
}
