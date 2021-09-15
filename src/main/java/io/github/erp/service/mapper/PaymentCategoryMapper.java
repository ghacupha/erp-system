package io.github.erp.service.mapper;

import io.github.erp.domain.*;
import io.github.erp.service.dto.PaymentCategoryDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentCategory} and its DTO {@link PaymentCategoryDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class })
public interface PaymentCategoryMapper extends EntityMapper<PaymentCategoryDTO, PaymentCategory> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "idSet")
    PaymentCategoryDTO toDto(PaymentCategory s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentCategoryDTO toDtoId(PaymentCategory paymentCategory);

    @Mapping(target = "removePlaceholder", ignore = true)
    PaymentCategory toEntity(PaymentCategoryDTO paymentCategoryDTO);
}
