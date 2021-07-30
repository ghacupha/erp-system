package io.github.erp.service.mapper;


import io.github.erp.domain.*;
import io.github.erp.service.dto.InvoiceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Invoice} and its DTO {@link InvoiceDTO}.
 */
@Mapper(componentModel = "spring", uses = {PaymentMapper.class, DealerMapper.class})
public interface InvoiceMapper extends EntityMapper<InvoiceDTO, Invoice> {

    @Mapping(source = "payment.id", target = "paymentId")
    @Mapping(source = "dealer.id", target = "dealerId")
    InvoiceDTO toDto(Invoice invoice);

    @Mapping(source = "paymentId", target = "payment")
    @Mapping(source = "dealerId", target = "dealer")
    Invoice toEntity(InvoiceDTO invoiceDTO);

    default Invoice fromId(Long id) {
        if (id == null) {
            return null;
        }
        Invoice invoice = new Invoice();
        invoice.setId(id);
        return invoice;
    }
}
