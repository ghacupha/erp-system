package io.github.erp.service.mapper;


import io.github.erp.domain.*;
import io.github.erp.service.dto.PaymentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring", uses = {PaymentCalculationMapper.class})
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {

    @Mapping(source = "paymentCalculation.id", target = "paymentCalculationId")
    PaymentDTO toDto(Payment payment);

    @Mapping(target = "ownedInvoices", ignore = true)
    @Mapping(target = "removeOwnedInvoice", ignore = true)
    @Mapping(source = "paymentCalculationId", target = "paymentCalculation")
    Payment toEntity(PaymentDTO paymentDTO);

    default Payment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Payment payment = new Payment();
        payment.setId(id);
        return payment;
    }
}
