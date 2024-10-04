package io.github.erp.internal.model.mapping;

import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.LeaseLiabilityReportItemREPO;
import io.github.erp.service.dto.LeaseLiabilityReportItemDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LeaseLiabilityReportREPOItemMapper extends Mapping<LeaseLiabilityReportItemDTO, LeaseLiabilityReportItemREPO> {
}
