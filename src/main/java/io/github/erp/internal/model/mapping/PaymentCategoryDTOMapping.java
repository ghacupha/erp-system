package io.github.erp.internal.model.mapping;

import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.PaymentCategoryBEO;
import io.github.erp.service.dto.PaymentCategoryDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentCategoryDTOMapping extends Mapping<PaymentCategoryBEO, PaymentCategoryDTO> {
}
