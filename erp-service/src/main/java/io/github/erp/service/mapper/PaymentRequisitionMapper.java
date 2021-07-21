package io.github.erp.service.mapper;


import io.github.erp.domain.*;
import io.github.erp.service.dto.PaymentRequisitionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentRequisition} and its DTO {@link PaymentRequisitionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PaymentRequisitionMapper extends EntityMapper<PaymentRequisitionDTO, PaymentRequisition> {


    @Mapping(target = "requisitions", ignore = true)
    @Mapping(target = "removeRequisition", ignore = true)
    PaymentRequisition toEntity(PaymentRequisitionDTO paymentRequisitionDTO);

    default PaymentRequisition fromId(Long id) {
        if (id == null) {
            return null;
        }
        PaymentRequisition paymentRequisition = new PaymentRequisition();
        paymentRequisition.setId(id);
        return paymentRequisition;
    }
}
