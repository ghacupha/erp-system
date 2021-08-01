package io.github.erp.service.mapper;

import io.github.erp.domain.*;
import io.github.erp.service.dto.PaymentCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentCategory} and its DTO {@link PaymentCategoryDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PaymentCategoryMapper extends EntityMapper<PaymentCategoryDTO, PaymentCategory> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentCategoryDTO toDtoId(PaymentCategory paymentCategory);
}
