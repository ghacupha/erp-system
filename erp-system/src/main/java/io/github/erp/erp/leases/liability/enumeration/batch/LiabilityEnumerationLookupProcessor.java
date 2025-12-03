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
import io.github.erp.domain.IFRS16LeaseContract;
import io.github.erp.domain.LeasePayment;
import io.github.erp.domain.LeasePaymentUpload;
import io.github.erp.domain.LiabilityEnumeration;
import io.github.erp.repository.IFRS16LeaseContractRepository;
import io.github.erp.repository.LeasePaymentRepository;
import io.github.erp.repository.LeasePaymentUploadRepository;
import io.github.erp.repository.LiabilityEnumerationRepository;
import java.util.List;
import java.util.Objects;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component("liabilityEnumerationLookupProcessor")
public class LiabilityEnumerationLookupProcessor implements ItemProcessor<LiabilityEnumerationBatchItem, LiabilityEnumerationBatchItem> {

    private final IFRS16LeaseContractRepository leaseContractRepository;
    private final LeasePaymentUploadRepository leasePaymentUploadRepository;
    private final LeasePaymentRepository leasePaymentRepository;
    private final LiabilityEnumerationRepository liabilityEnumerationRepository;

    public LiabilityEnumerationLookupProcessor(
        IFRS16LeaseContractRepository leaseContractRepository,
        LeasePaymentUploadRepository leasePaymentUploadRepository,
        LeasePaymentRepository leasePaymentRepository,
        LiabilityEnumerationRepository liabilityEnumerationRepository
    ) {
        this.leaseContractRepository = leaseContractRepository;
        this.leasePaymentUploadRepository = leasePaymentUploadRepository;
        this.leasePaymentRepository = leasePaymentRepository;
        this.liabilityEnumerationRepository = liabilityEnumerationRepository;
    }

    @Override
    public LiabilityEnumerationBatchItem process(LiabilityEnumerationBatchItem item) {
        IFRS16LeaseContract leaseContract = resolveLeaseContract(item.getRequest().getLeaseContractId());
        LeasePaymentUpload leasePaymentUpload = resolveLeasePaymentUpload(item.getRequest().getLeasePaymentUploadId());
        validateUploadMatchesContract(leasePaymentUpload, leaseContract);

        LiabilityEnumeration liabilityEnumeration = liabilityEnumerationRepository.save(
            new LiabilityEnumeration()
                .leaseContract(leaseContract)
                .leasePaymentUpload(leasePaymentUpload)
                .active(Boolean.TRUE.equals(item.getRequest().getActive()))
                .interestRate(item.getAnnualRate())
                .interestRateText(item.getRequest().getInterestRate())
                .timeGranularity(item.getGranularity())
        );

        List<LeasePayment> leasePayments = leasePaymentRepository.findAllByLeasePaymentUploadIdAndLeaseContractIdAndActiveTrueOrderByPaymentDateAsc(
            leasePaymentUpload.getId(),
            leaseContract.getId()
        );
        if (leasePayments.isEmpty()) {
            throw new IllegalArgumentException("No active lease payments found for the supplied upload and contract");
        }

        item.setLeaseContract(leaseContract);
        item.setLeasePaymentUpload(leasePaymentUpload);
        item.setLeasePayments(leasePayments);
        item.setLiabilityEnumeration(liabilityEnumeration);
        return item;
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
}
