package io.github.erp.service.mapper;

import io.github.erp.domain.Invoice;
import io.github.erp.service.dto.InvoiceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Invoice} and its DTO {@link InvoiceDTO}.
 */
@Mapper(componentModel = "spring", uses = { PaymentLabelMapper.class, PlaceholderMapper.class })
public interface InvoiceMapper extends EntityMapper<InvoiceDTO, Invoice> {
    @Mapping(target = "paymentLabels", source = "paymentLabels", qualifiedByName = "descriptionSet")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    InvoiceDTO toDto(Invoice s);

    @Mapping(target = "removePaymentLabel", ignore = true)
    @Mapping(target = "removePlaceholder", ignore = true)
    Invoice toEntity(InvoiceDTO invoiceDTO);
}
