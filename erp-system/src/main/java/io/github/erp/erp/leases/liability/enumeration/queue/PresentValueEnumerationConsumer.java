package io.github.erp.erp.leases.liability.enumeration.queue;

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
import io.github.erp.domain.LiabilityEnumeration;
import io.github.erp.domain.PresentValueEnumeration;
import io.github.erp.repository.IFRS16LeaseContractRepository;
import io.github.erp.repository.LiabilityEnumerationRepository;
import io.github.erp.repository.PresentValueEnumerationRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PresentValueEnumerationConsumer {

    private static final Logger log = LoggerFactory.getLogger(PresentValueEnumerationConsumer.class);

    private final LiabilityEnumerationRepository liabilityEnumerationRepository;
    private final IFRS16LeaseContractRepository leaseContractRepository;
    private final PresentValueEnumerationRepository presentValueEnumerationRepository;

    public PresentValueEnumerationConsumer(
        LiabilityEnumerationRepository liabilityEnumerationRepository,
        IFRS16LeaseContractRepository leaseContractRepository,
        PresentValueEnumerationRepository presentValueEnumerationRepository
    ) {
        this.liabilityEnumerationRepository = liabilityEnumerationRepository;
        this.leaseContractRepository = leaseContractRepository;
        this.presentValueEnumerationRepository = presentValueEnumerationRepository;
    }

    @KafkaListener(
        topics = "${spring.kafka.topics.present-value-enumeration.topic:present-value-enumeration}",
        groupId = "${spring.kafka.topics.present-value-enumeration.consumer.group.id:erp-system-present-value-enumeration}",
        containerFactory = "presentValueEnumerationKafkaListenerContainerFactory"
    )
    public void consume(PresentValueEnumerationQueueItem queueItem) {
        log.debug(
            "Consuming present value enumeration {} for liability enumeration {}",
            queueItem.getSequenceNumber(),
            queueItem.getLiabilityEnumerationId()
        );

        Optional<LiabilityEnumeration> enumerationOpt = liabilityEnumerationRepository.findById(queueItem.getLiabilityEnumerationId());
        Optional<IFRS16LeaseContract> leaseContractOpt = leaseContractRepository.findById(queueItem.getLeaseContractId());

        if (enumerationOpt.isEmpty() || leaseContractOpt.isEmpty()) {
            log.warn(
                "Skipping present value enumeration {} because related entities were not found",
                queueItem.getLiabilityEnumerationId()
            );
            return;
        }

        PresentValueEnumeration presentValueEnumeration = new PresentValueEnumeration()
            .sequenceNumber(queueItem.getSequenceNumber())
            .paymentDate(queueItem.getPaymentDate())
            .paymentAmount(queueItem.getPaymentAmount())
            .discountRate(queueItem.getDiscountRate())
            .presentValue(queueItem.getPresentValue())
            .leaseContract(leaseContractOpt.get())
            .liabilityEnumeration(enumerationOpt.get());

        presentValueEnumerationRepository.save(presentValueEnumeration);
    }
}
