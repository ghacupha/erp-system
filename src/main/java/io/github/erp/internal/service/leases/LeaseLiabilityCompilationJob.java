package io.github.erp.internal.service.leases;

import io.github.erp.service.dto.LeaseLiabilityCompilationDTO;

public interface LeaseLiabilityCompilationJob {

    void compileLeaseLiabilitySchedule(LeaseLiabilityCompilationDTO requestDTO);
}
