package io.github.erp.service.mapper;


import io.github.erp.domain.*;
import io.github.erp.service.dto.PaymentCategoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentCategory} and its DTO {@link PaymentCategoryDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PaymentCategoryMapper extends EntityMapper<PaymentCategoryDTO, PaymentCategory> {



    default PaymentCategory fromId(Long id) {
        if (id == null) {
            return null;
        }
        PaymentCategory paymentCategory = new PaymentCategory();
        paymentCategory.setId(id);
        return paymentCategory;
    }
}
