package io.github.erp.internal.service.rou;

import io.github.erp.service.dto.RouDepreciationRequestDTO;

public interface RouDepreciationValidationService {


    RouDepreciationRequestDTO invalidate(RouDepreciationRequestDTO rouDepreciationRequestDTO);
}
