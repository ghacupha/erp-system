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
import io.github.erp.domain.TAAmortizationRule;
import io.github.erp.domain.TAInterestPaidTransferRule;
import io.github.erp.domain.TALeaseInterestAccrualRule;
import io.github.erp.domain.TALeaseRecognitionRule;
import io.github.erp.domain.TALeaseRepaymentRule;
import io.github.erp.domain.TransactionAccount;
import io.github.erp.domain.TransactionDetails;
import io.github.erp.internal.repository.InternalApplicationUserRepository;
import io.github.erp.internal.repository.InternalLeaseLiabilityRepository;
import io.github.erp.internal.repository.InternalLeaseLiabilityScheduleItemRepository;
import io.github.erp.internal.repository.InternalRouDepreciationEntryRepository;
import io.github.erp.internal.repository.InternalRouInitialDirectCostRepository;
import io.github.erp.internal.repository.InternalTAAmortizationRuleRepository;
import io.github.erp.internal.repository.InternalTAInterestPaidTransferRuleRepository;
import io.github.erp.internal.repository.InternalTALeaseInterestAccrualRuleRepository;
import io.github.erp.internal.repository.InternalTALeaseRecognitionRuleRepository;
import io.github.erp.internal.repository.InternalTALeaseRepaymentRuleRepository;
import io.github.erp.internal.repository.InternalTransactionDetailsRepository;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
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

    private final InternalTransactionDetailsRepository transactionDetailsRepository;
    private final InternalLeaseLiabilityScheduleItemRepository leaseLiabilityScheduleItemRepository;
    private final InternalLeaseLiabilityRepository leaseLiabilityRepository;
    private final InternalRouInitialDirectCostRepository rouInitialDirectCostRepository;
    private final InternalRouDepreciationEntryRepository rouDepreciationEntryRepository;
    private final InternalTALeaseRepaymentRuleRepository leaseRepaymentRuleRepository;
    private final InternalTALeaseInterestAccrualRuleRepository leaseInterestAccrualRuleRepository;
    private final InternalTAInterestPaidTransferRuleRepository interestPaidTransferRuleRepository;
    private final InternalTALeaseRecognitionRuleRepository leaseRecognitionRuleRepository;
    private final InternalTAAmortizationRuleRepository amortizationRuleRepository;
    private final InternalApplicationUserRepository applicationUserRepository;
    private final TransactionEntryIdGenerator transactionEntryIdGenerator;

    public LeaseTransactionPostingServiceImpl(
        InternalTransactionDetailsRepository transactionDetailsRepository,
        InternalLeaseLiabilityScheduleItemRepository leaseLiabilityScheduleItemRepository,
        InternalLeaseLiabilityRepository leaseLiabilityRepository,
        InternalRouInitialDirectCostRepository rouInitialDirectCostRepository,
        InternalRouDepreciationEntryRepository rouDepreciationEntryRepository,
        InternalTALeaseRepaymentRuleRepository leaseRepaymentRuleRepository,
        InternalTALeaseInterestAccrualRuleRepository leaseInterestAccrualRuleRepository,
        InternalTAInterestPaidTransferRuleRepository interestPaidTransferRuleRepository,
        InternalTALeaseRecognitionRuleRepository leaseRecognitionRuleRepository,
        InternalTAAmortizationRuleRepository amortizationRuleRepository,
        InternalApplicationUserRepository applicationUserRepository,
        TransactionEntryIdGenerator transactionEntryIdGenerator
    ) {
        this.transactionDetailsRepository = transactionDetailsRepository;
        this.leaseLiabilityScheduleItemRepository = leaseLiabilityScheduleItemRepository;
        this.leaseLiabilityRepository = leaseLiabilityRepository;
        this.rouInitialDirectCostRepository = rouInitialDirectCostRepository;
        this.rouDepreciationEntryRepository = rouDepreciationEntryRepository;
        this.leaseRepaymentRuleRepository = leaseRepaymentRuleRepository;
        this.leaseInterestAccrualRuleRepository = leaseInterestAccrualRuleRepository;
        this.interestPaidTransferRuleRepository = interestPaidTransferRuleRepository;
        this.leaseRecognitionRuleRepository = leaseRecognitionRuleRepository;
        this.amortizationRuleRepository = amortizationRuleRepository;
        this.applicationUserRepository = applicationUserRepository;
        this.transactionEntryIdGenerator = transactionEntryIdGenerator;
    }

    @Override
    public void postLeaseRepayment(UUID requisitionId, Long postedById) {
        ApplicationUser postedBy = loadPostedBy(postedById);
        Map<Long, TALeaseRepaymentRule> rules = leaseRepaymentRuleRepository
            .findAll()
            .stream()
            .filter(rule -> rule.getLeaseContract() != null)
            .collect(Collectors.toMap(rule -> rule.getLeaseContract().getId(), Function.identity()));

        List<TransactionDetails> details = leaseLiabilityScheduleItemRepository
            .findAllWithLeaseContractAndPeriod()
            .stream()
            .filter(item -> hasNonZeroAmount(item.getCashPayment()))
            .map(item -> buildLeaseScheduleDetail(item, postedBy, requisitionId, LEASE_REPAYMENT_TYPE, "REPAYMENT", item.getCashPayment(),
                resolveRule(rules, item.getLeaseContract())))
            .collect(Collectors.toList());

        transactionDetailsRepository.saveAll(details);
    }

    @Override
    public void postLeaseInterestAccrual(UUID requisitionId, Long postedById) {
        ApplicationUser postedBy = loadPostedBy(postedById);
        Map<Long, TALeaseInterestAccrualRule> rules = leaseInterestAccrualRuleRepository
            .findAll()
            .stream()
            .filter(rule -> rule.getLeaseContract() != null)
            .collect(Collectors.toMap(rule -> rule.getLeaseContract().getId(), Function.identity()));

        List<TransactionDetails> details = leaseLiabilityScheduleItemRepository
            .findAllWithLeaseContractAndPeriod()
            .stream()
            .filter(item -> hasNonZeroAmount(item.getInterestAccrued()))
            .map(item -> buildLeaseScheduleDetail(item, postedBy, requisitionId, LEASE_INTEREST_ACCRUAL_TYPE, "INTEREST ACCRUED",
                item.getInterestAccrued(), resolveRule(rules, item.getLeaseContract())))
            .collect(Collectors.toList());

        transactionDetailsRepository.saveAll(details);
    }

    @Override
    public void postLeaseInterestPaidTransfer(UUID requisitionId, Long postedById) {
        ApplicationUser postedBy = loadPostedBy(postedById);
        Map<Long, TAInterestPaidTransferRule> rules = interestPaidTransferRuleRepository
            .findAll()
            .stream()
            .filter(rule -> rule.getLeaseContract() != null)
            .collect(Collectors.toMap(rule -> rule.getLeaseContract().getId(), Function.identity()));

        List<TransactionDetails> details = leaseLiabilityScheduleItemRepository
            .findAllWithLeaseContractAndPeriod()
            .stream()
            .filter(item -> hasNonZeroAmount(item.getCashPayment()))
            .map(item -> buildLeaseScheduleDetail(item, postedBy, requisitionId, LEASE_INTEREST_PAID_TRANSFER_TYPE, "INTEREST PAID",
                item.getInterestPayment(), resolveRule(rules, item.getLeaseContract())))
            .collect(Collectors.toList());

        transactionDetailsRepository.saveAll(details);
    }

    @Override
    public void postLeaseLiabilityRecognition(UUID requisitionId, Long postedById) {
        ApplicationUser postedBy = loadPostedBy(postedById);
        Map<Long, TALeaseRecognitionRule> rules = leaseRecognitionRuleRepository
            .findAll()
            .stream()
            .filter(rule -> rule.getLeaseContract() != null)
            .collect(Collectors.toMap(rule -> rule.getLeaseContract().getId(), Function.identity()));

        List<TransactionDetails> details = leaseLiabilityRepository
            .findAllWithLeaseContract()
            .stream()
            .map(liability -> buildLeaseLiabilityDetail(liability, postedBy, requisitionId, resolveRule(rules, liability.getLeaseContract())))
            .collect(Collectors.toList());

        transactionDetailsRepository.saveAll(details);
    }

    @Override
    public void postLeaseRouRecognition(UUID requisitionId, Long postedById) {
        ApplicationUser postedBy = loadPostedBy(postedById);
        List<TransactionDetails> details = rouInitialDirectCostRepository
            .findAllWithAccounts()
            .stream()
            .map(cost -> buildRouRecognitionDetail(cost, postedBy, requisitionId))
            .collect(Collectors.toList());

        transactionDetailsRepository.saveAll(details);
    }

    @Override
    public void postRouAmortization(UUID requisitionId, Long postedById) {
        ApplicationUser postedBy = loadPostedBy(postedById);
        Map<Long, TAAmortizationRule> rules = amortizationRuleRepository
            .findAll()
            .stream()
            .filter(rule -> rule.getLeaseContract() != null)
            .collect(Collectors.toMap(rule -> rule.getLeaseContract().getId(), Function.identity()));

        List<TransactionDetails> details = rouDepreciationEntryRepository
            .findAllWithLeaseContractAndPeriod()
            .stream()
            .map(entry -> buildRouAmortizationDetail(entry, postedBy, requisitionId, resolveRule(rules, entry.getLeaseContract())))
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

    private <T> T resolveRule(Map<Long, T> rules, IFRS16LeaseContract leaseContract) {
        Objects.requireNonNull(leaseContract, "Lease contract is required for posting.");
        T rule = rules.get(leaseContract.getId());
        if (rule == null) {
            throw new IllegalStateException("No posting rule found for lease contract " + leaseContract.getId());
        }
        return rule;
    }

    private TransactionDetails buildLeaseScheduleDetail(
        LeaseLiabilityScheduleItem item,
        ApplicationUser postedBy,
        UUID requisitionId,
        String transactionType,
        String suffix,
        BigDecimal amount,
        TALeaseRepaymentRule rule
    ) {
        return buildLeaseScheduleDetail(item, postedBy, requisitionId, transactionType, suffix, amount, rule.getDebit(), rule.getCredit());
    }

    private TransactionDetails buildLeaseScheduleDetail(
        LeaseLiabilityScheduleItem item,
        ApplicationUser postedBy,
        UUID requisitionId,
        String transactionType,
        String suffix,
        BigDecimal amount,
        TALeaseInterestAccrualRule rule
    ) {
        return buildLeaseScheduleDetail(item, postedBy, requisitionId, transactionType, suffix, amount, rule.getDebit(), rule.getCredit());
    }

    private TransactionDetails buildLeaseScheduleDetail(
        LeaseLiabilityScheduleItem item,
        ApplicationUser postedBy,
        UUID requisitionId,
        String transactionType,
        String suffix,
        BigDecimal amount,
        TAInterestPaidTransferRule rule
    ) {
        return buildLeaseScheduleDetail(item, postedBy, requisitionId, transactionType, suffix, amount, rule.getDebit(), rule.getCredit());
    }

    private TransactionDetails buildLeaseScheduleDetail(
        LeaseLiabilityScheduleItem item,
        ApplicationUser postedBy,
        UUID requisitionId,
        String transactionType,
        String suffix,
        BigDecimal amount,
        TransactionAccount debitAccount,
        TransactionAccount creditAccount
    ) {
        IFRS16LeaseContract leaseContract = Objects.requireNonNull(item.getLeaseContract(), "Lease contract is required for schedule entry.");
        LeaseRepaymentPeriod leasePeriod = Objects.requireNonNull(item.getLeasePeriod(), "Lease period is required for schedule entry.");
        Objects.requireNonNull(leasePeriod.getFiscalMonth(), "Fiscal month is required for schedule entry.");
        Objects.requireNonNull(amount, "Amount is required for schedule entry.");
        String fiscalMonthCode = leasePeriod.getFiscalMonth().getFiscalMonthCode();
        String description = leaseContract.getShortTitle() + " " + stripFiscalMonthCode(fiscalMonthCode) + suffix;

        TransactionDetails details = baseTransactionDetails(requisitionId, postedBy, transactionType);
        details.setTransactionDate(leasePeriod.getFiscalMonth().getEndDate());
        details.setDescription(description);
        details.setAmount(amount);
        details.setDebitAccount(debitAccount);
        details.setCreditAccount(creditAccount);
        return details;
    }

    private TransactionDetails buildLeaseLiabilityDetail(
        LeaseLiability leaseLiability,
        ApplicationUser postedBy,
        UUID requisitionId,
        TALeaseRecognitionRule rule
    ) {
        IFRS16LeaseContract leaseContract = Objects.requireNonNull(leaseLiability.getLeaseContract(), "Lease contract is required for liability.");
        Objects.requireNonNull(leaseLiability.getStartDate(), "Lease liability start date is required.");
        Objects.requireNonNull(leaseLiability.getLiabilityAmount(), "Lease liability amount is required.");
        String description = leaseContract.getShortTitle() + " LEASE RECOGNITION";

        TransactionDetails details = baseTransactionDetails(requisitionId, postedBy, LEASE_LIABILITY_RECOGNITION_TYPE);
        details.setTransactionDate(leaseLiability.getStartDate());
        details.setDescription(description);
        details.setAmount(leaseLiability.getLiabilityAmount());
        details.setDebitAccount(rule.getDebit());
        details.setCreditAccount(rule.getCredit());
        return details;
    }

    private TransactionDetails buildRouRecognitionDetail(RouInitialDirectCost directCost, ApplicationUser postedBy, UUID requisitionId) {
        IFRS16LeaseContract leaseContract = Objects.requireNonNull(directCost.getLeaseContract(), "Lease contract is required for ROU cost.");
        Objects.requireNonNull(directCost.getTransactionDate(), "ROU cost transaction date is required.");
        Objects.requireNonNull(directCost.getCost(), "ROU cost amount is required.");
        String reference = directCost.getReferenceNumber() == null ? "" : directCost.getReferenceNumber().toString();
        String description = "Ref#" + reference + " " + leaseContract.getShortTitle() + " INITIAL DIRECT COST";

        TransactionDetails details = baseTransactionDetails(requisitionId, postedBy, LEASE_ROU_RECOGNITION_TYPE);
        details.setTransactionDate(directCost.getTransactionDate());
        details.setDescription(description);
        details.setAmount(directCost.getCost());
        details.setDebitAccount(directCost.getTargetROUAccount());
        details.setCreditAccount(directCost.getTransferAccount());
        return details;
    }

    private TransactionDetails buildRouAmortizationDetail(
        RouDepreciationEntry entry,
        ApplicationUser postedBy,
        UUID requisitionId,
        TAAmortizationRule rule
    ) {
        Objects.requireNonNull(entry.getDepreciationAmount(), "Depreciation amount is required for ROU amortization.");
        TransactionDetails details = baseTransactionDetails(requisitionId, postedBy, ROU_AMORTIZATION_TYPE);
        Objects.requireNonNull(entry.getLeasePeriod(), "Lease period is required for ROU amortization.");
        details.setTransactionDate(entry.getLeasePeriod().getEndDate());
        details.setDescription(entry.getDescription());
        details.setAmount(entry.getDepreciationAmount());
        details.setDebitAccount(rule.getDebit());
        details.setCreditAccount(rule.getCredit());
        return details;
    }

    private TransactionDetails baseTransactionDetails(UUID requisitionId, ApplicationUser postedBy, String transactionType) {
        TransactionDetails details = new TransactionDetails();
        details.setEntryId(transactionEntryIdGenerator.nextEntryId());
        details.setPostingId(requisitionId);
        details.setPostedBy(postedBy);
        details.setTransactionType(transactionType);
        details.setIsDeleted(Boolean.FALSE);
        details.setCreatedAt(ZonedDateTime.now());
        return details;
    }

    private String stripFiscalMonthCode(String fiscalMonthCode) {
        return fiscalMonthCode == null ? "" : fiscalMonthCode.replace("YM", "");
    }
}
