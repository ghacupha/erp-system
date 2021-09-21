package io.github.erp.service.mapper;

import io.github.erp.domain.*;
import io.github.erp.service.dto.PaymentCalculationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentCalculation} and its DTO {@link PaymentCalculationDTO}.
 */
@Mapper(componentModel = "spring", uses = { PaymentLabelMapper.class, PaymentCategoryMapper.class, PlaceholderMapper.class })
public interface PaymentCalculationMapper extends EntityMapper<PaymentCalculationDTO, PaymentCalculation> {
    @Mapping(target = "paymentLabels", source = "paymentLabels", qualifiedByName = "descriptionSet")
    @Mapping(target = "paymentCategory", source = "paymentCategory", qualifiedByName = "id")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    PaymentCalculationDTO toDto(PaymentCalculation s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentCalculationDTO toDtoId(PaymentCalculation paymentCalculation);

    @Mapping(target = "removePaymentLabel", ignore = true)
    @Mapping(target = "removePlaceholder", ignore = true)
    PaymentCalculation toEntity(PaymentCalculationDTO paymentCalculationDTO);
}
