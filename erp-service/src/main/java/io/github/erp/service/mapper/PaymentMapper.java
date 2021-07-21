package io.github.erp.service.mapper;

import io.github.erp.domain.*;
import io.github.erp.service.dto.PaymentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentDTO toDtoId(Payment payment);
}
