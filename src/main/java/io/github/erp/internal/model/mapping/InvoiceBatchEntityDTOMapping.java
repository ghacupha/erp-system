package io.github.erp.internal.model.mapping;

import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.InvoiceBEO;
import io.github.erp.service.dto.InvoiceDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InvoiceBatchEntityDTOMapping extends Mapping<InvoiceBEO, InvoiceDTO> {
}
