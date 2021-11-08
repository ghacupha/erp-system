package io.github.erp.internal.model.mapping;

import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.DealerEVM;
import io.github.erp.service.dto.DealerDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DealerEVMMapping extends Mapping<DealerEVM, DealerDTO> {
}
