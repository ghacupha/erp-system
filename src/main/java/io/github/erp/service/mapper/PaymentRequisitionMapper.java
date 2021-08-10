package io.github.erp.service.mapper;

import io.github.erp.domain.*;
import io.github.erp.service.dto.PaymentRequisitionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentRequisition} and its DTO {@link PaymentRequisitionDTO}.
 */
@Mapper(componentModel = "spring", uses = { DealerMapper.class })
public interface PaymentRequisitionMapper extends EntityMapper<PaymentRequisitionDTO, PaymentRequisition> {
    @Mapping(target = "dealer", source = "dealer", qualifiedByName = "id")
    PaymentRequisitionDTO toDto(PaymentRequisition s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentRequisitionDTO toDtoId(PaymentRequisition paymentRequisition);
}
