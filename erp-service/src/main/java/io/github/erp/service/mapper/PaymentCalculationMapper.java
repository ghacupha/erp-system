package io.github.erp.service.mapper;


import io.github.erp.domain.*;
import io.github.erp.service.dto.PaymentCalculationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentCalculation} and its DTO {@link PaymentCalculationDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PaymentCalculationMapper extends EntityMapper<PaymentCalculationDTO, PaymentCalculation> {


    @Mapping(target = "calculationResult", ignore = true)
    PaymentCalculation toEntity(PaymentCalculationDTO paymentCalculationDTO);

    default PaymentCalculation fromId(Long id) {
        if (id == null) {
            return null;
        }
        PaymentCalculation paymentCalculation = new PaymentCalculation();
        paymentCalculation.setId(id);
        return paymentCalculation;
    }
}
