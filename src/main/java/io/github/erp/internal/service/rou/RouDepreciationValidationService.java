package io.github.erp.internal.service.rou;

import io.github.erp.service.dto.RouDepreciationRequestDTO;

public interface RouDepreciationValidationService {


    /**
     * Runs invalidation sequence for the rou-depreciation-request instance and all related
     * rou-depreciation-entry and affected rou-model-metadata instances
     *
     * @param rouDepreciationRequestDTO request subject to invalidation
     * @return Updated request
     */
    RouDepreciationRequestDTO invalidate(RouDepreciationRequestDTO rouDepreciationRequestDTO);

    /**
     * Runs revalidation sequence for the rou-depreciation-request instance and all related
     * rou-depreciation-entry and affected rou-model-metadata instances
     *
     * @param rouDepreciationRequestDTO request subject to revalidation
     * @return Updated request
     */
    RouDepreciationRequestDTO revalidate(RouDepreciationRequestDTO rouDepreciationRequestDTO);
}
