package io.github.erp.internal.service.leases;

import io.github.erp.service.dto.LeaseLiabilityScheduleItemDTO;

import java.util.List;

public interface LeaseAmortizationCompilationService {

    /**
     * Generates schedule items for a given lease liability
     * @param leaseLiabilityId id of the lease-liability instance
     * @return Schedule items
     */
    List<LeaseLiabilityScheduleItemDTO> generateAmortizationSchedule(Long leaseLiabilityId);
}
