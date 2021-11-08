package io.github.erp.internal.model.mapping;

import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.DealerBEO;
import io.github.erp.internal.model.DealerEVM;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DealerBatchEntityMapping extends Mapping<DealerEVM, DealerBEO> {
}
