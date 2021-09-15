package io.github.erp.service.mapper;

import io.github.erp.domain.*;
import io.github.erp.service.dto.InvoiceDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Invoice} and its DTO {@link InvoiceDTO}.
 */
@Mapper(componentModel = "spring", uses = { PaymentMapper.class, DealerMapper.class, PlaceholderMapper.class })
public interface InvoiceMapper extends EntityMapper<InvoiceDTO, Invoice> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "id")
    @Mapping(target = "dealer", source = "dealer", qualifiedByName = "id")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "idSet")
    InvoiceDTO toDto(Invoice s);

    @Mapping(target = "removePlaceholder", ignore = true)
    Invoice toEntity(InvoiceDTO invoiceDTO);
}
