package io.github.erp.service.mapper;


import io.github.erp.domain.*;
import io.github.erp.service.dto.PaymentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring", uses = {PaymentRequisitionMapper.class, TaxRuleMapper.class, PaymentCategoryMapper.class})
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {

    @Mapping(source = "paymentRequisition.id", target = "paymentRequisitionId")
    @Mapping(source = "taxRule.id", target = "taxRuleId")
    @Mapping(source = "paymentCategory.id", target = "paymentCategoryId")
    PaymentDTO toDto(Payment payment);

    @Mapping(target = "ownedInvoices", ignore = true)
    @Mapping(target = "removeOwnedInvoice", ignore = true)
    @Mapping(source = "paymentRequisitionId", target = "paymentRequisition")
    @Mapping(target = "dealers", ignore = true)
    @Mapping(target = "removeDealer", ignore = true)
    @Mapping(source = "taxRuleId", target = "taxRule")
    @Mapping(source = "paymentCategoryId", target = "paymentCategory")
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
