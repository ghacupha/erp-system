package io.github.erp.internal.model.mapping;

import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.SignedPaymentBEO;
import io.github.erp.service.dto.SignedPaymentDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SignedPaymentBatchEntityDTOMapping extends Mapping<SignedPaymentBEO, SignedPaymentDTO> {
}
