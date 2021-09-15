package io.github.erp.service.mapper;

import io.github.erp.domain.*;
import io.github.erp.service.dto.PaymentCalculationDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentCalculation} and its DTO {@link PaymentCalculationDTO}.
 */
@Mapper(componentModel = "spring", uses = { PaymentCategoryMapper.class, PlaceholderMapper.class })
public interface PaymentCalculationMapper extends EntityMapper<PaymentCalculationDTO, PaymentCalculation> {
    @Mapping(target = "paymentCategory", source = "paymentCategory", qualifiedByName = "id")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "idSet")
    PaymentCalculationDTO toDto(PaymentCalculation s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentCalculationDTO toDtoId(PaymentCalculation paymentCalculation);

    @Mapping(target = "removePlaceholder", ignore = true)
    PaymentCalculation toEntity(PaymentCalculationDTO paymentCalculationDTO);
}
