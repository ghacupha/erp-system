package io.github.erp.internal.model.mapping;

import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.PaymentLabelEVM;
import io.github.erp.service.dto.PaymentLabelDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentLabelEntityEVMMapping extends Mapping<PaymentLabelEVM, PaymentLabelDTO> {
}
