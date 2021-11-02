package io.github.erp.service.mapper;

import io.github.erp.domain.PaymentRequisition;
import io.github.erp.service.dto.PaymentRequisitionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentRequisition} and its DTO {@link PaymentRequisitionDTO}.
 */
@Mapper(componentModel = "spring", uses = { PaymentLabelMapper.class, DealerMapper.class, PlaceholderMapper.class })
public interface PaymentRequisitionMapper extends EntityMapper<PaymentRequisitionDTO, PaymentRequisition> {
    @Mapping(target = "paymentLabels", source = "paymentLabels", qualifiedByName = "descriptionSet")
    @Mapping(target = "dealer", source = "dealer", qualifiedByName = "id")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    PaymentRequisitionDTO toDto(PaymentRequisition s);

    @Mapping(target = "removePaymentLabel", ignore = true)
    @Mapping(target = "removePlaceholder", ignore = true)
    PaymentRequisition toEntity(PaymentRequisitionDTO paymentRequisitionDTO);
}
