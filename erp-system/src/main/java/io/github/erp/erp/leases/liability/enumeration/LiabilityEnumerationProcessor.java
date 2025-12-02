package io.github.erp.erp.leases.liability.enumeration;

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
import io.github.erp.domain.IFRS16LeaseContract;
import io.github.erp.domain.LeasePayment;
import io.github.erp.domain.LeasePaymentUpload;
import io.github.erp.domain.LiabilityEnumeration;
import io.github.erp.domain.enumeration.LiabilityTimeGranularity;
import io.github.erp.erp.leases.liability.enumeration.queue.PresentValueEnumerationProducer;
import io.github.erp.erp.leases.liability.enumeration.queue.PresentValueEnumerationQueueItem;
import io.github.erp.internal.service.leases.InternalLeaseAmortizationCalculationService;
import io.github.erp.repository.IFRS16LeaseContractRepository;
import io.github.erp.repository.LeasePaymentRepository;
import io.github.erp.repository.LeasePaymentUploadRepository;
import io.github.erp.repository.LiabilityEnumerationRepository;
import io.github.erp.service.dto.IFRS16LeaseContractDTO;
import io.github.erp.service.dto.LeaseAmortizationCalculationDTO;
import io.github.erp.service.mapper.IFRS16LeaseContractMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LiabilityEnumerationProcessor {

    private static final Logger log = LoggerFactory.getLogger(LiabilityEnumerationProcessor.class);

    private final IFRS16LeaseContractRepository leaseContractRepository;
    private final LeasePaymentUploadRepository leasePaymentUploadRepository;
    private final LeasePaymentRepository leasePaymentRepository;
    private final LiabilityEnumerationRepository liabilityEnumerationRepository;
    private final PresentValueCalculator presentValueCalculator;
    private final PresentValueEnumerationProducer presentValueEnumerationProducer;
    private final IFRS16LeaseContractMapper leaseContractMapper;
    private final InternalLeaseAmortizationCalculationService leaseAmortizationCalculationService;

    public LiabilityEnumerationProcessor(
        IFRS16LeaseContractRepository leaseContractRepository,
        LeasePaymentUploadRepository leasePaymentUploadRepository,
        LeasePaymentRepository leasePaymentRepository,
        LiabilityEnumerationRepository liabilityEnumerationRepository,
        PresentValueCalculator presentValueCalculator,
        PresentValueEnumerationProducer presentValueEnumerationProducer,
        IFRS16LeaseContractMapper leaseContractMapper,
        InternalLeaseAmortizationCalculationService leaseAmortizationCalculationService
    ) {
        this.leaseContractRepository = leaseContractRepository;
        this.leasePaymentUploadRepository = leasePaymentUploadRepository;
        this.leasePaymentRepository = leasePaymentRepository;
        this.liabilityEnumerationRepository = liabilityEnumerationRepository;
        this.presentValueCalculator = presentValueCalculator;
        this.presentValueEnumerationProducer = presentValueEnumerationProducer;
        this.leaseContractMapper = leaseContractMapper;
        this.leaseAmortizationCalculationService = leaseAmortizationCalculationService;
    }

    public LiabilityEnumerationResponse enumerate(LiabilityEnumerationRequest request) {
        LiabilityTimeGranularity granularity = LiabilityTimeGranularity.fromCode(request.getTimeGranularity());
        BigDecimal annualRate = new BigDecimal(request.getInterestRate());
        if (annualRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Interest rate must be zero or positive");
        }

        IFRS16LeaseContract leaseContract = resolveLeaseContract(request.getLeaseContractId());
        LeasePaymentUpload leasePaymentUpload = resolveLeasePaymentUpload(request.getLeasePaymentUploadId());
        validateUploadMatchesContract(leasePaymentUpload, leaseContract);

        LiabilityEnumeration liabilityEnumeration = liabilityEnumerationRepository.save(
            new LiabilityEnumeration()
                .leaseContract(leaseContract)
                .leasePaymentUpload(leasePaymentUpload)
                .active(Boolean.TRUE.equals(request.getActive()))
                .interestRate(annualRate)
                .interestRateText(request.getInterestRate())
                .timeGranularity(granularity)
        );

        List<LeasePayment> leasePayments = leasePaymentRepository.findAllByLeasePaymentUploadIdAndLeaseContractIdAndActiveTrueOrderByPaymentDateAsc(
            leasePaymentUpload.getId(),
            leaseContract.getId()
        );
        if (leasePayments.isEmpty()) {
            throw new IllegalArgumentException("No active lease payments found for the supplied upload and contract");
        }

        List<PresentValueLine> lines = presentValueCalculator.calculate(leasePayments, annualRate, granularity);
        lines.forEach(line -> dispatchQueueItem(liabilityEnumeration, leaseContract, line));

        LeaseAmortizationCalculationDTO amortizationCalculation = upsertLeaseAmortizationCalculation(
            leaseContract,
            granularity,
            annualRate,
            lines
        );

        LiabilityEnumerationResponse response = new LiabilityEnumerationResponse();
        response.setLiabilityEnumerationId(liabilityEnumeration.getId());
        response.setLeaseAmortizationCalculationId(amortizationCalculation.getId());
        response.setNumberOfPeriods(lines.size());
        response.setPeriodicity(String.valueOf(granularity.getCompoundsPerYear()));
        response.setTotalPresentValue(lines.stream().map(PresentValueLine::getPresentValue).reduce(BigDecimal.ZERO, BigDecimal::add));
        response.setDiscountRatePerPeriod(lines.isEmpty() ? BigDecimal.ZERO : lines.get(0).getDiscountRate());
        return response;
    }

    private IFRS16LeaseContract resolveLeaseContract(Long leaseContractId) {
        return leaseContractRepository
            .findById(leaseContractId)
            .orElseThrow(() -> new IllegalArgumentException("IFRS16 lease contract id #" + leaseContractId + " not found"));
    }

    private LeasePaymentUpload resolveLeasePaymentUpload(Long leasePaymentUploadId) {
        return leasePaymentUploadRepository
            .findById(leasePaymentUploadId)
            .orElseThrow(() -> new IllegalArgumentException("Lease payment upload id #" + leasePaymentUploadId + " not found"));
    }

    private void validateUploadMatchesContract(LeasePaymentUpload upload, IFRS16LeaseContract leaseContract) {
        if (upload.getLeaseContract() != null && !Objects.equals(upload.getLeaseContract().getId(), leaseContract.getId())) {
            throw new IllegalArgumentException("Lease payment upload does not belong to the provided lease contract");
        }
    }

    private void dispatchQueueItem(LiabilityEnumeration liabilityEnumeration, IFRS16LeaseContract leaseContract, PresentValueLine line) {
        PresentValueEnumerationQueueItem queueItem = new PresentValueEnumerationQueueItem();
        queueItem.setLiabilityEnumerationId(liabilityEnumeration.getId());
        queueItem.setLeaseContractId(leaseContract.getId());
        queueItem.setSequenceNumber(line.getSequenceNumber());
        queueItem.setPaymentDate(line.getPaymentDate());
        queueItem.setPaymentAmount(line.getPaymentAmount());
        queueItem.setDiscountRate(line.getDiscountRate());
        queueItem.setPresentValue(line.getPresentValue());
        presentValueEnumerationProducer.send(queueItem);
    }

    private LeaseAmortizationCalculationDTO upsertLeaseAmortizationCalculation(
        IFRS16LeaseContract leaseContract,
        LiabilityTimeGranularity granularity,
        BigDecimal annualRate,
        List<PresentValueLine> lines
    ) {
        BigDecimal totalPresentValue = lines
            .stream()
            .map(PresentValueLine::getPresentValue)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .setScale(2, RoundingMode.HALF_EVEN);

        LeaseAmortizationCalculationDTO existing = leaseAmortizationCalculationService
            .findByLeaseContractId(leaseContract.getId())
            .orElse(new LeaseAmortizationCalculationDTO());

        IFRS16LeaseContractDTO contractDto = leaseContractMapper.toDto(leaseContract);
        existing.setLeaseContract(contractDto);
        existing.setInterestRate(annualRate);
        existing.setPeriodicity(String.valueOf(granularity.getCompoundsPerYear()));
        existing.setLeaseAmount(totalPresentValue);
        existing.setNumberOfPeriods(lines.size());

        if (existing.getId() == null) {
            log.info("Creating lease amortization calculation for contract {}", leaseContract.getId());
            return leaseAmortizationCalculationService.save(existing);
        }

        log.info("Updating lease amortization calculation {} for contract {}", existing.getId(), leaseContract.getId());
        return leaseAmortizationCalculationService.update(existing);
    }
}
