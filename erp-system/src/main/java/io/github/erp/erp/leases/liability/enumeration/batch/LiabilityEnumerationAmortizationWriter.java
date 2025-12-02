package io.github.erp.erp.leases.liability.enumeration.batch;

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
import io.github.erp.erp.leases.liability.enumeration.LiabilityEnumerationResponse;
import io.github.erp.internal.service.leases.InternalLeaseAmortizationCalculationService;
import io.github.erp.service.dto.IFRS16LeaseContractDTO;
import io.github.erp.service.dto.LeaseAmortizationCalculationDTO;
import io.github.erp.service.mapper.IFRS16LeaseContractMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component("liabilityEnumerationAmortizationWriter")
public class LiabilityEnumerationAmortizationWriter
    implements ItemWriter<LiabilityEnumerationBatchItem>, StepExecutionListener {

    public static final String RESPONSE_CONTEXT_KEY = "liabilityEnumerationResponse";

    private final IFRS16LeaseContractMapper leaseContractMapper;
    private final InternalLeaseAmortizationCalculationService leaseAmortizationCalculationService;
    private StepExecution stepExecution;

    public LiabilityEnumerationAmortizationWriter(
        IFRS16LeaseContractMapper leaseContractMapper,
        InternalLeaseAmortizationCalculationService leaseAmortizationCalculationService
    ) {
        this.leaseContractMapper = leaseContractMapper;
        this.leaseAmortizationCalculationService = leaseAmortizationCalculationService;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED;
    }

    @Override
    public void write(List<? extends LiabilityEnumerationBatchItem> items) throws Exception {
        if (items.isEmpty()) {
            return;
        }

        LiabilityEnumerationBatchItem item = items.get(0);
        BigDecimal totalPresentValue = item
            .getPresentValueLines()
            .stream()
            .map(line -> line.getPresentValue())
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .setScale(2, RoundingMode.HALF_EVEN);

        LeaseAmortizationCalculationDTO existing = leaseAmortizationCalculationService
            .findByLeaseContractId(item.getLeaseContract().getId())
            .orElse(new LeaseAmortizationCalculationDTO());

        IFRS16LeaseContractDTO contractDto = leaseContractMapper.toDto(item.getLeaseContract());
        existing.setLeaseContract(contractDto);
        existing.setInterestRate(item.getAnnualRate());
        existing.setPeriodicity(String.valueOf(item.getGranularity().getCompoundsPerYear()));
        existing.setLeaseAmount(totalPresentValue);
        existing.setNumberOfPeriods(item.getPresentValueLines().size());

        LeaseAmortizationCalculationDTO amortization =
            existing.getId() == null ? leaseAmortizationCalculationService.save(existing) : leaseAmortizationCalculationService.update(existing);

        LiabilityEnumerationResponse response = new LiabilityEnumerationResponse();
        response.setLiabilityEnumerationId(item.getLiabilityEnumeration().getId());
        response.setLeaseAmortizationCalculationId(amortization.getId());
        response.setNumberOfPeriods(item.getPresentValueLines().size());
        response.setPeriodicity(String.valueOf(item.getGranularity().getCompoundsPerYear()));
        response.setTotalPresentValue(totalPresentValue);
        response.setDiscountRatePerPeriod(
            item.getPresentValueLines().isEmpty() ? BigDecimal.ZERO : item.getPresentValueLines().get(0).getDiscountRate()
        );
        item.setResponse(response);
        stepExecution.getJobExecution().getExecutionContext().put(RESPONSE_CONTEXT_KEY, response);
    }
}
