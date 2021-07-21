package io.github.erp.service.mapper;

import io.github.erp.domain.*;
import io.github.erp.service.dto.InvoiceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Invoice} and its DTO {@link InvoiceDTO}.
 */
@Mapper(componentModel = "spring", uses = { PaymentMapper.class })
public interface InvoiceMapper extends EntityMapper<InvoiceDTO, Invoice> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "id")
    InvoiceDTO toDto(Invoice s);
}
