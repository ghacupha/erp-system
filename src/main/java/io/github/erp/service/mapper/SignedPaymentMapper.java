package io.github.erp.service.mapper;

import io.github.erp.domain.*;
import io.github.erp.service.dto.SignedPaymentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SignedPayment} and its DTO {@link SignedPaymentDTO}.
 */
@Mapper(componentModel = "spring", uses = { PaymentLabelMapper.class, PlaceholderMapper.class })
public interface SignedPaymentMapper extends EntityMapper<SignedPaymentDTO, SignedPayment> {
    @Mapping(target = "paymentLabels", source = "paymentLabels", qualifiedByName = "descriptionSet")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "idSet")
    SignedPaymentDTO toDto(SignedPayment s);

    @Mapping(target = "removePaymentLabel", ignore = true)
    @Mapping(target = "removePlaceholder", ignore = true)
    SignedPayment toEntity(SignedPaymentDTO signedPaymentDTO);
}
