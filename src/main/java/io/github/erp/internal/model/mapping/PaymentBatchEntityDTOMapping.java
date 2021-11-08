package io.github.erp.internal.model.mapping;

import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.PaymentBEO;
import io.github.erp.service.dto.PaymentDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentBatchEntityDTOMapping extends Mapping<PaymentBEO, PaymentDTO> {
}
