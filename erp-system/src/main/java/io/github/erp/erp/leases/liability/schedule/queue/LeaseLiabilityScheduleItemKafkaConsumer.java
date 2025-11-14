package io.github.erp.erp.leases.liability.schedule.queue;

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

import io.github.erp.domain.LeaseAmortizationSchedule;
import io.github.erp.domain.LeaseLiability;
import io.github.erp.domain.LeaseLiabilityCompilation;
import io.github.erp.domain.LeaseLiabilityScheduleItem;
import io.github.erp.domain.LeaseRepaymentPeriod;
import io.github.erp.erp.leases.liability.schedule.model.LeaseLiabilityScheduleItemQueueItem;
import io.github.erp.repository.LeaseAmortizationScheduleRepository;
import io.github.erp.repository.LeaseLiabilityCompilationRepository;
import io.github.erp.repository.LeaseLiabilityRepository;
import io.github.erp.repository.LeaseLiabilityScheduleItemRepository;
import io.github.erp.repository.LeaseRepaymentPeriodRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class LeaseLiabilityScheduleItemKafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(LeaseLiabilityScheduleItemKafkaConsumer.class);

    private final LeaseLiabilityScheduleItemRepository scheduleItemRepository;
    private final LeaseLiabilityRepository leaseLiabilityRepository;
    private final LeaseAmortizationScheduleRepository leaseAmortizationScheduleRepository;
    private final LeaseLiabilityCompilationRepository leaseLiabilityCompilationRepository;
    private final LeaseRepaymentPeriodRepository leaseRepaymentPeriodRepository;
    private final String topicName;

    public LeaseLiabilityScheduleItemKafkaConsumer(
        LeaseLiabilityScheduleItemRepository scheduleItemRepository,
        LeaseLiabilityRepository leaseLiabilityRepository,
        LeaseAmortizationScheduleRepository leaseAmortizationScheduleRepository,
        LeaseLiabilityCompilationRepository leaseLiabilityCompilationRepository,
        LeaseRepaymentPeriodRepository leaseRepaymentPeriodRepository,
        @Value("${spring.kafka.topics.lease-liability-schedule.topic.name:lease-liability-schedule-items}") String topicName
    ) {
        this.scheduleItemRepository = scheduleItemRepository;
        this.leaseLiabilityRepository = leaseLiabilityRepository;
        this.leaseAmortizationScheduleRepository = leaseAmortizationScheduleRepository;
        this.leaseLiabilityCompilationRepository = leaseLiabilityCompilationRepository;
        this.leaseRepaymentPeriodRepository = leaseRepaymentPeriodRepository;
        this.topicName = topicName;
    }

    @KafkaListener(
        topics = "${spring.kafka.topics.lease-liability-schedule.topic.name:lease-liability-schedule-items}",
        containerFactory = "leaseLiabilityScheduleKafkaListenerContainerFactory"
    )
    @Transactional
    public void consume(LeaseLiabilityScheduleItemQueueItem queueItem) {
        log.debug("Received lease liability schedule item {} from topic {}", queueItem.getUuid(), topicName);

        LeaseLiabilityScheduleItem entity = new LeaseLiabilityScheduleItem();
        entity.setSequenceNumber(queueItem.getSequenceNumber());
        entity.setOpeningBalance(queueItem.getOpeningBalance());
        entity.setCashPayment(queueItem.getCashPayment());
        entity.setPrincipalPayment(queueItem.getPrincipalPayment());
        entity.setInterestPayment(queueItem.getInterestPayment());
        entity.setOutstandingBalance(queueItem.getOutstandingBalance());
        entity.setInterestPayableOpening(queueItem.getInterestPayableOpening());
        entity.setInterestAccrued(queueItem.getInterestAccrued());
        entity.setInterestPayableClosing(queueItem.getInterestPayableClosing());
        entity.setActive(queueItem.getActive() != null ? queueItem.getActive() : Boolean.TRUE);

        LeaseLiability leaseLiability = leaseLiabilityRepository
            .findById(queueItem.getLeaseLiabilityId())
            .orElseThrow(() -> new IllegalStateException("Lease liability not found for id " + queueItem.getLeaseLiabilityId()));
        entity.setLeaseLiability(leaseLiability);
        entity.setLeaseContract(leaseLiability.getLeaseContract());

        if (queueItem.getLeaseAmortizationScheduleId() != null) {
            Optional<LeaseAmortizationSchedule> amortizationSchedule =
                leaseAmortizationScheduleRepository.findById(queueItem.getLeaseAmortizationScheduleId());
            amortizationSchedule.ifPresent(entity::setLeaseAmortizationSchedule);
        }

        LeaseLiabilityCompilation compilation = leaseLiabilityCompilationRepository
            .findById(queueItem.getLeaseLiabilityCompilationId())
            .orElseThrow(
                () -> new IllegalStateException(
                    "Lease liability compilation not found for id " + queueItem.getLeaseLiabilityCompilationId()
                )
            );
        entity.setLeaseLiabilityCompilation(compilation);

        LeaseRepaymentPeriod leaseRepaymentPeriod = leaseRepaymentPeriodRepository
            .findById(queueItem.getLeasePeriodId())
            .orElseThrow(() -> new IllegalStateException("Lease repayment period not found for id " + queueItem.getLeasePeriodId()));
        entity.setLeasePeriod(leaseRepaymentPeriod);

        scheduleItemRepository.save(entity);
    }
}

