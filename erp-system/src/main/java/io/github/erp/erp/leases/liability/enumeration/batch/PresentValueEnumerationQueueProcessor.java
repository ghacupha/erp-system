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
import io.github.erp.erp.leases.liability.enumeration.PresentValueCalculator;
import io.github.erp.erp.leases.liability.enumeration.PresentValueLine;
import io.github.erp.erp.leases.liability.enumeration.queue.PresentValueEnumerationProducer;
import io.github.erp.erp.leases.liability.enumeration.queue.PresentValueEnumerationQueueItem;
import java.util.List;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component("liabilityEnumerationQueueProcessor")
public class PresentValueEnumerationQueueProcessor implements ItemProcessor<LiabilityEnumerationBatchItem, LiabilityEnumerationBatchItem> {

    private final PresentValueCalculator presentValueCalculator;
    private final PresentValueEnumerationProducer presentValueEnumerationProducer;

    public PresentValueEnumerationQueueProcessor(
        PresentValueCalculator presentValueCalculator,
        PresentValueEnumerationProducer presentValueEnumerationProducer
    ) {
        this.presentValueCalculator = presentValueCalculator;
        this.presentValueEnumerationProducer = presentValueEnumerationProducer;
    }

    @Override
    public LiabilityEnumerationBatchItem process(LiabilityEnumerationBatchItem item) {
        List<PresentValueLine> lines = presentValueCalculator.calculate(
            item.getLeasePayments(),
            item.getAnnualRate(),
            item.getGranularity()
        );

        lines.forEach(line -> dispatchQueueItem(item, line));
        item.setPresentValueLines(lines);
        return item;
    }

    private void dispatchQueueItem(LiabilityEnumerationBatchItem item, PresentValueLine line) {
        PresentValueEnumerationQueueItem queueItem = new PresentValueEnumerationQueueItem();
        queueItem.setLiabilityEnumerationId(item.getLiabilityEnumeration().getId());
        queueItem.setLeaseContractId(item.getLeaseContract().getId());
        queueItem.setSequenceNumber(line.getSequenceNumber());
        queueItem.setPaymentDate(line.getPaymentDate());
        queueItem.setPaymentAmount(line.getPaymentAmount());
        queueItem.setDiscountRate(line.getDiscountRate());
        queueItem.setPresentValue(line.getPresentValue());
        presentValueEnumerationProducer.send(queueItem);
    }
}
