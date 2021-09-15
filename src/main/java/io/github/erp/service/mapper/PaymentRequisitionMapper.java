package io.github.erp.service.mapper;

import io.github.erp.domain.*;
import io.github.erp.service.dto.PaymentRequisitionDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentRequisition} and its DTO {@link PaymentRequisitionDTO}.
 */
@Mapper(componentModel = "spring", uses = { DealerMapper.class, PlaceholderMapper.class })
public interface PaymentRequisitionMapper extends EntityMapper<PaymentRequisitionDTO, PaymentRequisition> {
    @Mapping(target = "dealer", source = "dealer", qualifiedByName = "id")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "idSet")
    PaymentRequisitionDTO toDto(PaymentRequisition s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentRequisitionDTO toDtoId(PaymentRequisition paymentRequisition);

    @Mapping(target = "removePlaceholder", ignore = true)
    PaymentRequisition toEntity(PaymentRequisitionDTO paymentRequisitionDTO);
}
