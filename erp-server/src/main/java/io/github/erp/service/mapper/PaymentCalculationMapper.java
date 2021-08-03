package io.github.erp.service.mapper;

import io.github.erp.domain.*;
import io.github.erp.service.dto.PaymentCalculationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentCalculation} and its DTO {@link PaymentCalculationDTO}.
 */
@Mapper(componentModel = "spring", uses = { PaymentCategoryMapper.class })
public interface PaymentCalculationMapper extends EntityMapper<PaymentCalculationDTO, PaymentCalculation> {
    @Mapping(target = "paymentCategory", source = "paymentCategory", qualifiedByName = "id")
    PaymentCalculationDTO toDto(PaymentCalculation s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentCalculationDTO toDtoId(PaymentCalculation paymentCalculation);
}
