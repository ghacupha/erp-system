package io.github.erp.internal.service.rou;

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

import io.github.erp.internal.model.RouDepreciationScheduleViewInternal;
import io.github.erp.internal.repository.InternalRouDepreciationEntryRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RouDepreciationScheduleViewServiceImpl implements RouDepreciationScheduleViewService {

    private final Logger log = LoggerFactory.getLogger(RouDepreciationScheduleViewServiceImpl.class);

    private final InternalRouDepreciationEntryRepository rouDepreciationEntryRepository;

    public RouDepreciationScheduleViewServiceImpl(InternalRouDepreciationEntryRepository rouDepreciationEntryRepository) {
        this.rouDepreciationEntryRepository = rouDepreciationEntryRepository;
    }

    @Override
    public List<RouDepreciationScheduleViewInternal> getScheduleView(Long leaseContractId) {
        log.debug("Request to get ROU depreciation schedule view for lease contract: {}", leaseContractId);
        List<RouDepreciationScheduleViewInternal> schedule = rouDepreciationEntryRepository.getRouDepreciationScheduleView(leaseContractId);

        if (schedule.isEmpty()) {
            return schedule;
        }

        List<RouDepreciationScheduleViewInternal> adjustedSchedule = new ArrayList<>(schedule.size());
        BigDecimal previousOutstanding = null;

        for (int index = 0; index < schedule.size(); index++) {
            RouDepreciationScheduleViewInternal row = schedule.get(index);
            BigDecimal initialAmount;
            if (index == 0) {
                initialAmount = row.getInitialAmount();
            } else {
                initialAmount = previousOutstanding != null ? previousOutstanding : row.getInitialAmount();
            }

            adjustedSchedule.add(
                new BasicRouDepreciationScheduleView(
                    row.getEntryId(),
                    row.getSequenceNumber(),
                    row.getLeaseNumber(),
                    row.getPeriodCode(),
                    row.getPeriodStartDate(),
                    row.getPeriodEndDate(),
                    initialAmount,
                    row.getDepreciationAmount(),
                    row.getOutstandingAmount()
                )
            );

            previousOutstanding = row.getOutstandingAmount();
        }

        return adjustedSchedule;
    }

    private static final class BasicRouDepreciationScheduleView implements RouDepreciationScheduleViewInternal {

        private final Long entryId;
        private final Integer sequenceNumber;
        private final String leaseNumber;
        private final String periodCode;
        private final LocalDate periodStartDate;
        private final LocalDate periodEndDate;
        private final BigDecimal initialAmount;
        private final BigDecimal depreciationAmount;
        private final BigDecimal outstandingAmount;

        private BasicRouDepreciationScheduleView(
            Long entryId,
            Integer sequenceNumber,
            String leaseNumber,
            String periodCode,
            LocalDate periodStartDate,
            LocalDate periodEndDate,
            BigDecimal initialAmount,
            BigDecimal depreciationAmount,
            BigDecimal outstandingAmount
        ) {
            this.entryId = entryId;
            this.sequenceNumber = sequenceNumber;
            this.leaseNumber = leaseNumber;
            this.periodCode = periodCode;
            this.periodStartDate = periodStartDate;
            this.periodEndDate = periodEndDate;
            this.initialAmount = initialAmount;
            this.depreciationAmount = depreciationAmount;
            this.outstandingAmount = outstandingAmount;
        }

        @Override
        public Long getEntryId() {
            return entryId;
        }

        @Override
        public Integer getSequenceNumber() {
            return sequenceNumber;
        }

        @Override
        public String getLeaseNumber() {
            return leaseNumber;
        }

        @Override
        public String getPeriodCode() {
            return periodCode;
        }

        @Override
        public LocalDate getPeriodStartDate() {
            return periodStartDate;
        }

        @Override
        public LocalDate getPeriodEndDate() {
            return periodEndDate;
        }

        @Override
        public BigDecimal getInitialAmount() {
            return initialAmount;
        }

        @Override
        public BigDecimal getDepreciationAmount() {
            return depreciationAmount;
        }

        @Override
        public BigDecimal getOutstandingAmount() {
            return outstandingAmount;
        }
    }
}
